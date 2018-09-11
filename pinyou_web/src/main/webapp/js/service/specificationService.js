app.service("specificationService",function($http){
    this.search=function(pageNum,pageSize,entity){
        return $http.post("../specification/search?pageNum="+pageNum+"&pageSize="+pageSize,entity);
    }
    this.add=function(entity){
        return $http.post("../specification/add",entity);
    }
    this.findOne=function(id){
        return $http.get("../specification/findOne?id="+id);
    }
    this.update=function(entity){
        return $http.post("../specification/update",entity);
    }
    this.dele=function(ids){
        return $http.get("../specification/delete?ids="+ids);
    }

    //查询规格下拉列表
    this.findSpecList = function () {
        return $http.get("../specification/findSpecList");
    }
})