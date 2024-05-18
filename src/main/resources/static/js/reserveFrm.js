$("#reserve-btn").prop("disabled", true);//클릭 못하게

$.ajax({
  url: "/reserve/closedDays",
  type: "post",
  data: {storeNo: storeNo}, //매개변수로 건내주는 data는 반드시 객체{key : value} 형태로!!!
  dataType : "json",
  success: function (data1) {
    $.ajax({
      url: "/reserve/tempClosedDays",
      type: "post",
      data: {storeNo:storeNo},
      dataType : "json",
      success: function (data2){
        //비활성화할거 배열(순서때매 이거 먼저 선언해야함)
        var closedDays = []; //정기휴무일(요일인데 숫자로)
        var tempClosedDays = []; //임시휴무일(날짜)
        for(let i=0; i<data1.length; i++){
          closedDays.push(data1[i]);
        };
        for(let i=0; i<data2.length; i++){
          tempClosedDays.push(data2[i]);
        };

        $('#datepicker').datepicker({
          //데이트 포맷
          dateFormat: "yy-mm-dd",
          //한글로 표시
          prevText: "이전 달",
          nextText: "다음 달",
          monthNames: ["1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"],
          dayNames: ["일","월","화","수","목","금","토"],
          dayNamesShort: ["일","월","화","수","목","금","토"],
          dayNamesMin: ["일","월","화","수","목","금","토"],
          //선택 가능 기간 : 최소 오늘부터, 최대 2주 후까지
          minDate: "0",
          maxDate: "+2W",
          //특정일 선택 비활성화하기
          beforeShowDay: disableSomeDays,
          //날짜 선택시 작동할 함수(input태그가 아니라 div태그에 datepicker 넣었기 때문에)
          onSelect: select
        });

        function disableSomeDays(date){
          var month = date.getMonth();
          var dates = date.getDate();
          var year = date.getFullYear();
          var day = date.getDay();

          //정기휴무일(요일)
          for(let i=0; i<closedDays.length; i++){
            if (day == closedDays[i]){
              return [false];
            }
          }

          //잠간 꼽사리) 휴무일일때 영업시간의 시각을 "휴무일"로 대체하고 빨간색
          for(let i=0; i<closedDays.length; i++){
            $("[name='openingHour']").eq(closedDays[i]).text("휴무일");
            $("[name='openingHour']").eq(closedDays[i]).css("color", "red");
          }
          //잠간 꼽사리) 휴게시간 없을 때, 휴게시간란을 비움
          if($("#breakTime").text() == "-1 ~ -1"){
            $("#breakTime").text("");
          }

          //임시휴무일(날짜)
          //배열(disabledDays)의 값과 일치하는 날짜는 false를 리턴
          for (let i = 0; i < tempClosedDays.length; i++) {
            if(month < 9){
              if(dates < 10){
                if($.inArray(year + '-0' + (month+1) + '-0' + dates, tempClosedDays) != -1){
                  return [false];
                }
              } else{
                if($.inArray(year + '-0' + (month+1) + '-' + dates, tempClosedDays) != -1){
                  return [false];
                }
              }
            } else{
              if(dates < 10){
                if($.inArray(year + '-0' + (month+1) + '-0' + dates, tempClosedDays) != -1){
                  return [false];
                }
              } else{
                if($.inArray(year + '-0' + (month+1) + '-' + dates, tempClosedDays) != -1){
                  return [false];
                }
              }
            }
          }
          return [true];
        };

        //시작하자마자 오늘날짜로 클릭해놓기
        //(선생님이 도와주심...아니,,해주심...)
        $(".ui-datepicker-today").click();

        //날짜 선택시 작동할 함수
        function select(dateText){
          //숨겨둔 input태그에 날짜 집어넣기
          $("#reserveDate").val(dateText);
          //시각 버튼들 리셋 + reserveDate input태그 리셋
          
          $("#reserveTime").val("");
          //인원수 리셋
          $("#people").text(1);
          //timeSet 가져오기
          $.ajax({
            url:"/reserve/timeSet",
            type: "post",
            data: {storeNo: storeNo, selectedDay: dateText},
            dataType: "json",
            success: function(timeSet){
              //시각 버튼들 리셋
              $(".reserveTime").remove();
              // timeSet.allTimes = List<String> allTimes
              // timeSet.fullTimes  = List<String> fullTimes
              // timeSet.remainTimes = List<String> remainTimes 이건 안 쓰네??
              //시간 버튼들 생성
              for(let i=0; i<timeSet.allTimes.length; i++){
                const time = timeSet.allTimes[i];
                const button = $("<button>");
                button.attr("type", "button");
                button.addClass("reserveTime smallBtn bg-green");
                button.text(time);
                //그 시각이 만석인 시각일 때
                if(timeSet.fullTimes.indexOf(time) != -1){
                  button.removeClass("reserveTime smallBtn");
                  button.addClass("fullTime bg-gray reserveTime smallBtn");//fulltime 클래스 추가
                  button.prop("disabled", true);//클릭 못하게
                };
                $(".time-area .li-content").append(button);
              };
              

              //예약완료버튼 활성화(뒤에 또 있음)
              if($("#reserveDate").val() != ""
              && $("#reserveTime").val() != ""){
                $("#reserve-btn").prop("disabled", false);//클릭 가능하게
                $("#reserve-btn").addClass("getReady");
              }else{
                $("#reserve-btn").prop("disabled", true);//클릭 못하게
                $("#reserve-btn").removeClass("getReady");
              }

              //reserveTime버튼 클릭시 함수
              $(".reserveTime").on("click",function(){
                //1. class 리셋
                $(".reserveTime").removeClass("smallBtn-active");
                //2. 클릭한 버튼에만 class 추가
                $(this).addClass("smallBtn-active");
                //3. hidden으로 숨긴 input태그에 값 추가
                $("#reserveTime").val($(this).text());
                //4. 인원수 리셋
                $("#people").text(1);
                //5. 예약 가능 인원수 구하기
                $.ajax({
                  url: "/reserve/tableNoAndCapacity",
                  type: "post",
                  data: {
                    storeNo: storeNo,
                    reserveDate: dateText,
                    reserveTime: $("#reserveTime").val()
                  },
                  dataType: "json",
                  success: function(tableNoAndCapacity){
                    //최대 수용 가능 인원 구하기. 식탁 수용가능 인원수가 적은 것 부터 index 0 번에 배치됨
                    const maxNum = tableNoAndCapacity[tableNoAndCapacity.length - 1].tableCapacity;
                    //인원수 세팅
                    let peopleNum = Number($("#people").text());
                    const tableNoArr = [];
                    const tableCapacityArr = [];
                    for(let i=0; i<tableNoAndCapacity.length; i++){
                      tableNoArr.push(tableNoAndCapacity[i].tableNo);
                      tableCapacityArr.push(tableNoAndCapacity[i].tableCapacity);
                    }
                    $("#people").text(peopleNum);
                    //hidden으로 숨긴 input태그에 값 추가(reservePeople 그리고 tableNo)
                    searchTable(peopleNum, tableNoArr, tableCapacityArr);
                    //-1
                    $("#people-minus").on("click",function(){
                      if(peopleNum>1){
                        peopleNum -= 1;
                        $("#people").text(peopleNum);
                        //hidden으로 숨긴 input태그에 값 추가(reservePeople 그리고 tableNo)
                        searchTable(peopleNum, tableNoArr, tableCapacityArr);
                      }
                    });
                    //+1
                    $("#people-plus").on("click",function(){
                      if(peopleNum < maxNum){
                        peopleNum += 1;
                        $("#people").text(peopleNum);
                        //hidden으로 숨긴 input태그에 값 추가(reservePeople 그리고 tableNo)
                        searchTable(peopleNum, tableNoArr, tableCapacityArr);
                      }
                    });
                    
                    function searchTable(peopleNum, tableNoArr, tableCapacityArr){
                      for(let i=0; i<tableNoArr.length; i++){
                        if(peopleNum <= tableCapacityArr[i]){
                          $("#reservePeople").val(peopleNum);
                          $("#tableNo").val(tableNoArr[i]);
                          break;
                        }
                      }
                    }


                    //예약완료버튼 활성화
                    if($("#reserveDate").val() != ""
                    && $("#reserveTime").val() != ""){
                      $("#reserve-btn").prop("disabled", false);//클릭 가능하게
                      $("#reserve-btn").addClass("getReady");
                    }else{
                      $("#reserve-btn").prop("disabled", true);//클릭 못하게
                      $("#reserve-btn").removeClass("getReady");
                    }

                  },
                  error: function(){
                    console.log("error");
                  }
                });

              });
            },
            error: function(){
              console.log("error");
            }
          })
          
        };
        


      },
      error: function(){
		console.log("에러");
      }
    });

  },
  error : function(){
    console.log("에러");
  }
});




//메뉴와 servings 함수
//-
$("#servings-minus").on("click",function(){
  let servingsNum = Number($("#servings").text());
  if(servingsNum > 1){
    servingsNum -= 1;
    $("#servings").text(servingsNum);
  }
});
//+
$("#servings-plus").on("click",function(){
  let servingsNum = Number($("#servings").text());
  servingsNum += 1;
  $("#servings").text(servingsNum);
});
//메뉴 관련 정보 저장해둘 배열 선언
const menuNoArr = [];
const servingsArr = [];
const menuNameArr = [];
//추가 버튼 클릭시
$("#menu-order-btn").on("click",function(){
  //input태그 생성 및 값 추가
  const inputMenuNo = $("<input>");
  inputMenuNo.attr("type", "hidden");
  inputMenuNo.attr("name","menuNo");
  const menuNo = $(".menu-list > option:selected").val();
  inputMenuNo.attr("value", menuNo);
  const inputServings = $("<input>");
  inputServings.attr("type", "hidden");
  inputServings.attr("name","servings");
  const servings = $("#servings").text();
  inputServings.attr("value", servings);
  //input태그를 menu-area 아래에 넣기
  $(".menu-area").append(inputMenuNo);
  $(".menu-area").append(inputServings);
  //배열들에 값 저장해놓기
  menuNoArr.push(menuNo);
  servingsArr.push(servings);
  menuNameArr.push($(".menu-list > option:selected").text());
  //메뉴 리셋해주기
  $("#servings").text("1");






// 추가된 메뉴 보여주기
const div = $("<div>");
const span1 = $("<span>");
span1.attr("name", "showMenuName");
span1.text("- " + menuNameArr[menuNoArr.length - 1] + " : ");
const span2 = $("<span>");
span2.attr("name", "showServings");
span2.text(servingsArr[menuNoArr.length - 1] + "인분");
const span3 = $("<span>");
span3.attr("name", "deleteMenuBtn");
span3.text("(x) ");
div.append(span1).append(span2).append(span3);
$(".selected-menu").append(div);
// 삭제버튼 클릭시 함수
span3.on("click", function(){
  // 현재 삭제 버튼의 인덱스 찾기
  const index = $(".selected-menu [name='deleteMenuBtn']").index(this);
  // 배열에서 해당 인덱스의 값들 삭제
  $("[name='menuNo']").eq(index).remove();
  $("[name='servings']").eq(index).remove();
  $("[name='showMenuName']").eq(index).remove();
  $("[name='showServings']").eq(index).remove();
  $("[name='deleteMenuBtn']").eq(index).remove();
  // 배열에서도 삭제
  menuNoArr.splice(index, 1);
  servingsArr.splice(index, 1);
  menuNameArr.splice(index, 1);

  /*
  //추가된 메뉴 보여주기
  const span1 = $("<span>");
  span1.attr("name", "showMenuName");
  span1.text(menuNameArr[menuNoArr.length - 1] + " : ");
  const span2 = $("<span>");
  span2.attr("name", "showMenuServings");
  span2.text(servingsArr[menuNoArr.length-1] + "인분");
  const span3 = $("<span>");
  span3.attr("name", "deleteMenuBtn");
  span3.text("(삭제)  ");
  $(".selected-menu").append(span1).append(span2).append(span3);
  //삭제버튼 클릭시 함수
  $("[name='deleteMenuBtn']").on("click",function(){
    let index = $("[name='deleteMenuBtn']").index(this);
    $("[name='menuNo']").eq(index).remove();
    $("[name='servings']").eq(index).remove();
    $("[name='showMenuName']").eq(index).remove();
    $("[name='showMenuServings']").eq(index).remove();
    $("[name='deleteMenuBtn']").eq(index).remove();
    menuNoArr.splice(index, 1);
    servingsArr.splice(index, 1);
    menuNameArr.splice(index, 1);
    */

  });
});

$('.reserveRequest').on('keyup', function() {
  $('#requestCount').text("("+$(this).val().length+" / 66)");

  if($(this).val().length > 66) {
      $(this).val($(this).val().substring(0, 66));
      $('#requestCount').text("(66 / 66)");
  }
});





