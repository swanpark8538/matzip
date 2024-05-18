/*녹색 칠하고, 서브메뉴 있으면 보여주기 - 박성완*/
const list = $(".list");
list.on("click", function(){
  list.children("a").removeClass("active");
  $(this).children("a").addClass("active");
  $(this).children("ul").children().toggleClass("sub-active");
});

/*서브메뉴 있으면 그 상위 a태그 우측 끝에 더보기 아이콘 추가 - 박성완*/
const span = $("<span class='material-icons'>");
span.attr("class", "material-icons");
span.text("expand_more");
const subList = $(".sub-list");
subList.parent().prev("a").append(span);

