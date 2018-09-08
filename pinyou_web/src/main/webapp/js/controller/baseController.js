app.controller("baseController",function($scope){

    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,//当前页
        totalItems: 10,//总记录数
        itemsPerPage: 10,//每页显示记录数
        perPageOptions: [10, 20, 30, 40, 50],//下拉框
        onChange: function () {
            //重新加载,此方法将会被自动加载
            //1,页面刷新
            //2,分页控件中数据发生变化，reloadList也会自动调用
            $scope.reloadList();
        }
    };

    //定义一个刷新页面的方法
    $scope.reloadList=function(){
        $scope.search($scope.paginationConf.currentPage,
            $scope.paginationConf.itemsPerPage);
    }

    //定义一个数组来存放ID
    $scope.selectIds=[];

    //定义一个方法：点击复选框事件把id放在数组中的
    $scope.updateSelection=function($event,id){
        //$event	: 事件对象
        //$event.target：点击的复选框对象
        if($event.target.checked){//选中
            $scope.selectIds.push(id);
        } else{
            //来获取ID在数组中的索引
            var index = $scope.selectIds.indexOf(id);
            //参数说明：1、指定删除的索引；2、指定删除的个数
            $scope.selectIds.splice(index,1);
        }
    }


});