/**
 * 
 */

//검색한 주소의 정보를 insertAddress 함수로 넘겨준다.
function searchAddressToCoordinate(address) {
  naver.maps.Service.geocode({
    query: address
  }, function (status, response) {
    if (status === naver.maps.Service.Status.ERROR) {
      return null;
    }
    if (response.v2.meta.totalCount === 0) {
      return null;
    }
    const htmlAddresses = [],
      item = response.v2.addresses[0],
      point = new naver.maps.Point(item.x, item.y);
    if (item.roadAddress) {
      htmlAddresses.push('[도로명 주소] ' + item.roadAddress);
    }
    if (item.jibunAddress) {
      htmlAddresses.push('[지번 주소] ' + item.jibunAddress);
    }
    if (item.englishAddress) {
      htmlAddresses.push('[영문명 주소] ' + item.englishAddress);
    }
    insertAddress(item.roadAddress, item.x, item.y);
  });
}

//검색정보를 테이블로 작성해주고, 지도에 마커를 찍어준다.
function insertAddress(address, latitude, longitude) {
  const mapList = "";
  $('#mapList').append(mapList);

  const map = new naver.maps.Map('map', {
    center: new naver.maps.LatLng(longitude, latitude),
    zoom: 16
  });
  const marker = new naver.maps.Marker({
    map: map,
    position: new naver.maps.LatLng(longitude, latitude),
  });

  let infoWindow = new naver.maps.InfoWindow();

  // marker에 클릭이벤트 추가
  naver.maps.Event.addListener(marker, "click", function (e) {
    // infoWindow객체에 우리가 만들어놓은 태그문자열을 세팅
    infoWindow = new naver.maps.InfoWindow({
      content: contentString
    });
    infoWindow.open(map, marker);
  });
}

//지도를 그려주는 함수
function selectMapList() {
  const map = new naver.maps.Map('map', {
    center: new naver.maps.LatLng(37.3595704, 127.105399),
    zoom: 10,
    zoomControl: true,
    zoomControlOptions: {
      position: naver.maps.Position.TOP_RIGHT,
      style: naver.maps.ZoomControlStyle.SMALL
    }
  });
}

// 지도를 이동하게 해주는 함수
function moveMap(len, lat) {
  const mapOptions = {
    center: new naver.maps.LatLng(len, lat),
    zoom: 16,
    mapTypeControl: true
  };
  const map = new naver.maps.Map('map', mapOptions);
  const marker = new naver.maps.Marker({
    position: new naver.maps.LatLng(len, lat),
    map: map
  });
}

function submitForm(action) {
  const form = document.getElementById('infoForm');
  if (action === 'update') {
    form.action = '/search/updateInfo';
  } else if (action === 'insert') {
    form.action = '/search/insertInfo';
  }
  form.submit();
}

function infoAlert() {
  alert("최초 작성시에는 작성 버튼을 눌러 작성해주세요.");
}

function modifyReview(obj, reviewNo, storeNo) {
  $(obj).parent().prev().show();
  $(obj).parent().prev().prev().hide();
  $(obj).text("수정완료");
  $(obj).attr("onclick", "modifyComplete(this," + reviewNo + "," + storeNo + ")");
  $(obj).next().text("수정취소");
  $(obj).next().attr("onclick", "modifyCancel(this," + reviewNo + "," + storeNo + ")");
  $(obj).next().next().hide();
}
function modifyCancel(obj, reviewNo, storeNo) {
  $(obj).parent().prev().hide();
  $(obj).parent().prev().prev().show();
  $(obj).prev().text("수정");
  $(obj).prev().attr("onclick", "modifyReview(this," + reviewNo + "," + storeNo + ")");
  $(obj).text("삭제");
  $(obj).attr("onclick", "deleteReview(this," + reviewNo + "," + storeNo + ")");
  $(obj).next().show();
}

function modifyComplete(obj, reviewNo, storeNo) {
  // 1. form태그 생성
  const form = $("<form style='display:none;' action='/search/updateReview' method='post'>");
  // 2. 전송할 데이터를 form 내부의 자식으로 추가(reviewNo, storeNo)
  const reviewNoInput = $("<input type='text' name='reviewNo'>");
  reviewNoInput.val(reviewNo);
  const storeNoInput = $("<input type='text' name='storeNo'>");
  storeNoInput.val(storeNo);
  // 3. textarea(reviewContent)
  const reviewContent = $(obj).parent().prev().clone();
  // 4. form태그에 input태그 2개와, textarea태그를 모두 포함
  form.append(reviewNoInput).append(storeNoInput).append(reviewContent);
  // 5. 생성한 폼태그를 현재 문서 내부에 포함
  $("body").append(form);
  // 6. form태그 전송
  form.submit();
}

function deleteReview(obj, reviewNo, storeNo) {
  if (confirm("리뷰를 삭제하시겠습니까?")) {
    location.href = "/search/deleteReview?reviewNo=" + reviewNo + "&storeNo=" + storeNo;
  }
}

document.addEventListener("DOMContentLoaded", function () {
  var reviews = document.querySelectorAll('.reviewBox .posting-review');
  var moreBtn = document.getElementById('more-btn');
  var startIndex = 5;
  var reviewsPerPage = 5;

  // 처음 5개의 리뷰를 제외한 나머지 리뷰를 숨김
  for (var i = reviewsPerPage; i < reviews.length; i++) {
    reviews[i].style.display = 'none';
  }

  // "더보기" 버튼을 클릭하면 다음 5개의 리뷰를 보여줌
  moreBtn.addEventListener('click', function () {
    var endIndex = startIndex + reviewsPerPage;
    for (var i = startIndex; i < endIndex; i++) {
      if (i < reviews.length) {
        reviews[i].style.display = 'flex';
      }
    }
    startIndex += reviewsPerPage;
    // "더보기" 버튼을 숨김
    if (startIndex >= reviews.length) {
      moreBtn.style.display = 'none';
    }
  });
  
  // 초기에 리뷰가 5개 이하일 때만 "더보기" 버튼을 표시
  if (reviews.length <= 5) {
    moreBtn.style.display = 'none';
  }
});


