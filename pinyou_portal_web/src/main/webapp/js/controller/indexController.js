app.controller("indexController",function($scope,indexService){

    //因为你查询的时候不一定只查首页广告轮播图的，有可能会查询多个，所以咱们放在数组中
    $scope.contentList=[];

    $scope.findContentByCategoryId=function(categoryId){
        indexService.findContentByCategoryId(categoryId).success(function (response) {
            $scope.contentList[categoryId] = response;
        })
    }

});