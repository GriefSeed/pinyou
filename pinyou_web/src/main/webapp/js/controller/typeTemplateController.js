 //控制层 
app.controller('typeTemplateController' ,function($scope,$controller,typeTemplateService, brandService, specificationService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		typeTemplateService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		typeTemplateService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		typeTemplateService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=typeTemplateService.update( $scope.entity ); //修改  
		}else{
			serviceObject=typeTemplateService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		typeTemplateService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		typeTemplateService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

	//定义方法，查询品牌下拉列表
    $scope.findBrandList = function () {
        //调用service服务方法，实现品牌列表查询
        brandService.findBrandList().success(function (data) {
            $scope.brandList = {data: data};
        })

    }

    //定义方法，查询品牌下拉列表
    $scope.findSpecList = function () {
        //调用service服务方法，实现品牌列表查询
        specificationService.findSpecList().success(function (data) {
            $scope.specList = {data: data};
        })

    }

    //定义动态添加扩展属性行的操作
    //entity.customAttributeItems = [{},{}]
    $scope.addTableRow = function () {
        //添加空对象
        $scope.entity.customAttributeItems.push({});
    };

    //删除扩展属性行
    $scope.delTableRow = function (index) {
        $scope.entity.customAttributeItems.splice(index,1);
    }

    //查询实体
    $scope.findOne = function (id) {
        typeTemplateService.findOne(id).success(
            function (response) {
                $scope.entity = response;
                //把品牌json字符串转换成json对象
                $scope.entity.brandIds = JSON.parse($scope.entity.brandIds);
                //把规格json字符串转换成json对象
                $scope.entity.specIds = JSON.parse($scope.entity.specIds);
                //扩展属性
                $scope.entity.customAttributeItems = JSON.parse($scope.entity.customAttributeItems);
    }
        );
    }

});	
