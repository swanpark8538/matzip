

$.ajax({
  url: "/reserve/closedDays2",
  type: "post",
  data: {storeNo: storeNo}, //매개변수로 건내주는 data는 반드시 객체{key : value} 형태로!!!
  dataType : "json",
  success: function (data1) {
    $.ajax({
      url: "/reserve/tempClosedDays2",
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

        $("#datepicker").datepicker({
          //데이트 포맷
          dateFormat: "yy-mm-dd",
          //한글로 표시
          prevText: "이전 달",
          nextText: "다음 달",
          monthNames: ["1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"],
          dayNames: ["일","월","화","수","목","금","토"],
          dayNamesShort: ["일","월","화","수","목","금","토"],
          dayNamesMin: ["일","월","화","수","목","금","토"],
          //선택 가능 기간 : 최소 오늘부터, 최대 4주 후까지
          //일반 이용자는 2주 후까지만 선택 가능
          //매장에게 "특정일 비활성화", 즉 임시휴무일 기능을 부여하기 위해
          minDate: "0",
          maxDate: "+4W",
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

          //임시휴무일(날짜)
          //배열(disabledDays)의 값과 일치하는 날짜는 false를 리턴
          for (let i = 0; i < tempClosedDays.length; i++) {
            if(month < 9){
              if($.inArray(year + '-0' + (month+1) + '-' + dates, tempClosedDays) != -1){
                return [false];
              }
            } else{
              if($.inArray(year + '-' + (month+1) + '-' + dates, tempClosedDays) != -1){
                return [false];
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
          //예약내역 생성한 것들 초기화
          $(".reserveList .article-content").empty();
          //timeSet 가져오기
          $.ajax({
            url:"/reserve/timeSet2",
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
                };
                $(".timeset .article-content").append(button);
              };

              //reserveTime클래스 버튼 클릭시 함수
              $(".reserveTime").on("click",function(){
                //1. class 리셋
                $(".reserveTime").removeClass("smallBtn-active");
                //2. 클릭한 버튼에만 class 추가
                $(this).addClass("smallBtn-active");
                //3. hidden으로 숨긴 input태그에 값 추가
                $("#reserveTime").val($(this).text());
                //4. 예약내역 생성한 것들 초기화
                $(".reserveList .article-content").empty();
                //5. 해당 시각의 예약건들 출력
                $.ajax({
                  url:"/reserve/reserveListStore",
                  type: "post",
                  data: {
                    storeNo: storeNo,
                    reserveDate: $("#reserveDate").val(),
                    reserveTime: $("#reserveTime").val()
                  },
                  dataType: "json",
                  success: function(reserveViewStoreList){
                    const reserveList = reserveViewStoreList.reserveList;
                    const menuServingsList = reserveViewStoreList.menuServingsList;
                    //예약내역 생성해서 append
                    for(let i=0; i<reserveList.length; i++){
                      //small-article
                      const smallArticle = $("<article>");
                      smallArticle.addClass("small-article reserve");
                      //name
                      const name = $("<div>");
                      name.addClass("name");
                        const nameTitle = $("<div>");
                          nameTitle.addClass("small-title");
                          nameTitle.text("예약자");
                        const nameContent = $("<div>");
                          nameContent.addClass("small-content bolder")
                          nameContent.text(reserveList[i].memberName);
                      name.append(nameTitle).append(nameContent);

                      //people
                      const people = $("<div>");
                      people.addClass("people");
                        const peopleTitle = $("<div>");
                          peopleTitle.addClass("small-title");
                          peopleTitle.text("예약인원");
                        const peopleContent = $("<div>");
                          peopleContent.addClass("small-content")
                          peopleContent.text(reserveList[i].reservePeople);
                        people.append(peopleTitle).append(peopleContent);
                      //menu
                      const menu = $("<div>");
                      menu.addClass("menu");
                        const menuTitle = $("<div>");
                          menuTitle.addClass("small-title");
                          menuTitle.text("메뉴");
                        const menuContent = $("<div>");
                          menuContent.addClass("small-content")
                          for(let j=0; j<menuServingsList.length; j++){
                            if(menuServingsList[j].reserveNo == reserveList[i].reserveNo){
                              const menuName = menuServingsList[j].menuName;
                              const servings = menuServingsList[j].servings;
                              const menuServingsDiv = $("<div>");
                              menuServingsDiv.text(menuName +"("+servings+"인분)");
                              menuContent.append(menuServingsDiv);
                            }
                          }
                      menu.append(menuTitle).append(menuContent);
                      //request
                      const request = $("<div>");
                      request.addClass("request");
                        const requestTitle = $("<div>");
                          requestTitle.addClass("small-title");
                          requestTitle.text("요청사항");
                        const requestContent = $("<div>");
                          requestContent.addClass("small-content")
                            const requestDiv = $("<div>");
                            requestDiv.text(reserveList[i].reserveRequest);
                          requestContent.append(requestDiv);
                      request.append(requestTitle).append(requestContent);
                      //button(reserveNo)
                      const now = new Date();
                      const cancelBtn = $("<button>");
                        //잠간 날짜비교를 위해
                        var today = new Date();
                        var rr = new Date(reserveList[i].reserveDate)
                      if((rr-today)/(1000*60*60*24) > 1){
                        cancelBtn.attr("type","button");
                        cancelBtn.addClass("middle-btn bg-red cancel-btn");
                        cancelBtn.val(reserveList[i].reserveNo);
                        cancelBtn.text("예약취소");
                      }else{
                        cancelBtn.attr("type","button");
                        cancelBtn.addClass("middle-btn bg-gray no-cancel-btn");
                        cancelBtn.text("예약취소 불가");
                      }
                      //추가
                      smallArticle.append(name).append(people).append(menu).append(request).append(cancelBtn);
                      $(".reserveList .article-content").append(smallArticle);

                      $(".cancel-btn").on("click",function(){
                        const cancleBtn2 = $(this);
                        const reserveNo2 = $(this).val();
                        $.ajax({
                          url: "/reserve/cancelReserve2",
                          type: "post",
                          data: {reserveNo: reserveNo2},
                          dataType: "json",
                          success: function(result){
                            if(result>0){
                              cancleBtn2.parent().addClass("article-cancel");
                              setTimeout(function(){
                                location.replace("/reserve/reserveManage");
                              }, 1000);
                            }else{
                              const failDiv = $("<div>");
                              failDiv.text("취소 실패")
                              cancleBtn2.parent().append(failDiv);
                            }
                          },
                          error: function(result){
                            const failDiv = $("<div>");
                            failDiv.text("취소 실패")
                            cancleBtn2.parent().append(failDiv);
                          }
                        })
                      })
                    }
                  },
                  error: function(){
                    console.log("error");
                  },
                })
              });


            },
            error: function(){
              console.log("error");
            }
          })
          
        };
        


      },
      error: function(){
        console.log("error");
      }
    });

  },
  error : function(){
    console.log("error");
  }
});


$("#InsertTempBtn").on("click",function(){
  $.ajax({
    url: "/reserve/insertTemp",
    type: "post",
    data: {
      storeNo : storeNo,
      insertTempDay : $("#reserveDate").val()
    },
    dataType: "json",
    success: function(result){
      location.replace("/reserve/reserveManage");
    },
    error: function(){
      console.log("error");
    }
  })
})

$("#deleteTempBtn").on("click",function(){
  $.ajax({
    url: "/reserve/deleteTemp",
    type: "post",
    data: {
      storeNo : storeNo,
      deleteTempDay : $("#reserveDate").val()
    },
    dataType: "json",
    success: function(result){
      location.replace("/reserve/reserveManage");
    },
    error: function(){
      console.log("error");
    }
  })
})