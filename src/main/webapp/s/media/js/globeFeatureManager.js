
//调用说明：添加feature
//var position = myGlobalCtrl.CreatePoint3d();
//position.SetValue(102.436414875, 139.93129409843963, 0);
//globeFeatureManager.addFeature("设备图层","张帆","DEVICETYPE","vehicle","model/红色轿车.gcm",position,0);

//调用说明：删除feature，layer为图层对象
//globeFeatureManager.deleteFeature(“featureName”,layer)

//更新模型路径
//globeFeatureManager.updateFilepathAndFieldvalue(“featureName”,layer,modelpath)

var globeFeatureManager={
	addFeature:function(layerName,featureName,fieldName,fieldValue,modelPath,position,altitudeMode){
		if(ds!=null){
			for(var i = 0;i<ds.Count;i++){
				if(ds.Item(i).Caption==layerName){
					var layer=myGlobalCtrl.Globe.Layers.Item(ds.Item(i).Caption);
					if(layer!=null){
					var feature = layer.Dataset.CreateFeature();  
					var model = myGlobalCtrl.CreateGeoModel();
					model.FilePath=modelPath;
					model.Position=position;
					model.AltitudeMode=altitudeMode;
					feature.Geometry = model;
					feature.Name = featureName;
					feature.SetFieldValue(fieldName, fieldValue);//第一个参数是 字段的名称，使用字段名称作为第一个参数的函数没有封装出来
					
					//修复bug GXZTCGXT_BUG_035(1、编号以及编码都显示空白)
					feature.SetFieldValue("井深", 0);
					feature.SetFieldValue("编码", featureName);
					
					layer.AddFeature(feature);					
					layer.Save();
					}
				}
			}
		}
		else{
		alert("增加feature失败");
		}
	},
	deleteFeature:function(featureName,layer){
	
		if(featureName != null && layer != null){
		var features= layer.GetFeatureByName(featureName,false);
		 for(var i=0;i<features.Count;i++){
			feature = features.Item(i);
			feature.Delete();
		 }
		layer.Save();
		myGlobalCtrl.Refresh();
		}
	},
	
	updateFilepathAndFieldvalue:function(featureName,layer,modelPath){
	
		if(layer == null||featureName==null||modelPath==null){
			return;
		}
		var features = layer.GetFeatureByName(featureName,false);
		for ( var j = features.Count -1; j >= 0; j--) {
			var feature = features.Item(j);
			if (feature != null && feature.Geometry!=null && feature.Geometry.Type==305) {
				var model = feature.Geometry;			
				model.FilePath = modelPath;		
				layer.save();
			}
		}
	}
}
	
	
	
	