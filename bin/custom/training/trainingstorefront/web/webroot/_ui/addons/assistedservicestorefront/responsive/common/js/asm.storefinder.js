/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

ASM.storefinder = {
	storeData:"",
	storeId:"",
	originalStore:"",
	coords:{},
	storeSearchData:{},
	originAddress:"",

	autoLoad: function (data) {
		originAddress=data;
		ASM.storefinder.init();
		ASM.storefinder.bindStoreChange();
		ASM.storefinder.bindPagination();
	},
	
	
	
	createListItemHtml: function (data,id){

		var item="";
		item+='<li class="asm__list__entry">';
		item+='<input type="radio" name="storeNamePost" value="'+data.displayName+'" id="asm-store-filder-entry-'+id+'" class="js-asm-store-finder-input" data-id="'+id+'">';
		item+='<label for="asm-store-filder-entry-'+id+'" class="js-select-store-label">';
		item+='<span class="entry__info">';
		item+='<span class="entry__name">'+data.displayName+'</span>';
		item+='<span class="entry__address">'+data.line1+' '+data.line2+'</span>';
		item+='<span class="entry__city">'+data.town+'</span>';
		item+='</span>';
		item+='<span class="entry__distance">';
		item+='<span>'+data.formattedDistance+'</span>';
		item+='</span>';
		item+='</label>';
		item+='</li>';
		return item;
	},

	refreshNavigation: function (){
		var listitems = "";
		data = ASM.storefinder.storeData;
		
		if(data){
			for(i = 0;i < data["data"].length;i++){
				listitems += ASM.storefinder.createListItemHtml(data["data"][i],i);
			}
	
			$(".js-asm-store-finder-navigation-list").html(listitems);
	
			// select the first store
			var firstInput= $(".js-asm-store-finder-input")[0];
			$(firstInput).click();
		}


		var page = ASM.storefinder.storeSearchData.page;
		$(".js-asm-store-finder-pager-item-from").html(page*10+1);

		var to = ((page*10+10)>ASM.storefinder.storeData.total)? ASM.storefinder.storeData.total : page*10+10 ;
		$(".js-asm-store-finder-pager-item-to").html(to);
		$(".js-asm-store-finder-pager-item-all").html(ASM.storefinder.storeData.total);
		$(".js-asm-store-finder").removeClass("show-store");

	},


	bindPagination:function ()
	{
		$(document).on("click",".js-asm-store-finder-pager-prev",function(e){
			e.preventDefault();
			var page = ASM.storefinder.storeSearchData.page;
			ASM.storefinder.getStoreData(page-1);
			checkStatus(page-1);
		});

		$(document).on("click",".js-asm-store-finder-pager-next",function(e){
			e.preventDefault();
			var page = ASM.storefinder.storeSearchData.page;
			ASM.storefinder.getStoreData(page+1);
			checkStatus(page+1);
		});

		function checkStatus(page){
			if(page===0){
				$(".js-asm-store-finder-pager-prev").attr("disabled","disabled");
			}else{
				$(".js-asm-store-finder-pager-prev").removeAttr("disabled");
			}
			
			if(page === Math.floor(ASM.storefinder.storeData.total/10)){
				$(".js-asm-store-finder-pager-next").attr("disabled","disabled");
			}else{
				$(".js-asm-store-finder-pager-next").removeAttr("disabled");
			}
		}

	},

	bindStoreChange:function()
	{
		$(document).on("change",".js-asm-store-finder-input",function(e){
			e.preventDefault();



			storeData=ASM.storefinder.storeData["data"];

			var storeId=$(this).data("id");

			var $ele = $(".js-asm-store-finder-details");
			


			$.each(storeData[storeId],function(key,value){
				if(key==="image"){
					if(value!==""){
						$ele.find(".js-asm-store-image").html('<img src="'+value+'" alt="" />');
					}else{
						$ele.find(".js-asm-store-image").html('');
					}
				}else if(key==="productcode"){
					$ele.find(".js-asm-store-productcode").val(value);
				}
				else if(key==="openings"){
					if(value!==""){
						var $oele = $ele.find(".js-asm-store-"+key);
						var openings = "";
						$.each(value,function(key2,value2){
							openings += "<dt>"+key2+"</dt>";
							openings += "<dd>"+value2+"</dd>";
						});

						$oele.html(openings);

					}else{
						$ele.find(".js-asm-store-"+key).html('');
					}

				}
				else if(key==="features"){
					var features="";
					$.each(value,function(key2,value2){
						features += "<li>"+value2+"</li>";
					});

					$ele.find(".js-asm-store-"+key).html(features);

				}
				else{
					if(value!==""){
						$ele.find(".js-asm-store-"+key).html(value);
					}else{
						$ele.find(".js-asm-store-"+key).html('');
					}
				}

			});

			ASM.storefinder.storeId = storeData[storeId];
			ASM.storefinder.initGoogleMap();

		});
	},



	initGoogleMap:function(){

		if($(".js-asm-store-finder-map").length > 0){
			ACC.global.addGoogleMapsApi("ASM.storefinder.loadGoogleMap");
		}
	},
 
	loadGoogleMap: function(){

		storeInformation = ASM.storefinder.storeId;

		if($(".js-asm-store-finder-map").length > 0)
		{			
			$(".js-asm-store-finder-map").attr("id","asm-store-finder-map");
			var centerPoint = new google.maps.LatLng(storeInformation["latitude"], storeInformation["longitude"]);
			
			var mapOptions = {
				zoom: 16,
				zoomControl: true,
				panControl: true,
				streetViewControl: false,
				mapTypeId: google.maps.MapTypeId.ROADMAP,
				center: centerPoint
			};
			
			var map = new google.maps.Map(document.getElementById("asm-store-finder-map"), mapOptions);
			
			// Prevent the store info has been stored, when the first page is loaded, which is needed only for the driving instructions. 
			if(ASM.storefinder.originalStore === "" ) {
				ASM.storefinder.originalStore = data["data"][0];
			}

			//Driving Options
			if(ACC.config.googleApiKey !== "" && ASM.storefinder.originalStore["latitude"] !== storeInformation["latitude"]) {
				var directionsDisplay = new google.maps.DirectionsRenderer();
				var directionsService = new google.maps.DirectionsService();
				var originPoint = new google.maps.LatLng(ASM.storefinder.originalStore["latitude"], ASM.storefinder.originalStore["longitude"]);
				directionsDisplay.setMap(map);
				var request = {
					origin: originPoint,
					destination: centerPoint,
					travelMode: 'DRIVING'
				};
				
				directionsService.route(request, function(response, status) {
				    if (status === 'OK') {
				      directionsDisplay.setDirections(response);
				    }
				  });				
			}
			// Driving Options
			
			var marker = new google.maps.Marker({
				position: new google.maps.LatLng(storeInformation["latitude"], storeInformation["longitude"]),
				map: map,
				title: storeInformation["name"],
				icon: "https://maps.google.com/mapfiles/marker" + 'A' + ".png"
			});
			
			var infowindow = new google.maps.InfoWindow({
				content: ACC.common.encodeHtml(storeInformation["name"]),
				disableAutoPan: true
			});
			
			google.maps.event.addListener(marker, 'click', function (){
				infowindow.open(map, marker);
			});
			
			var markerPosition=storeInformation["latitude"]+","+ storeInformation["longitude"];
			map.addListener('click', function(e) {
				if(ACC.config.googleApiKey !== "")
			{
			window.open("https://www.google.de/maps/dir/"+originAddress+"/"+markerPosition,'_blank');
		}
		
		else
		{
			window.open("https://www.google.de/maps/dir/"+markerPosition,'_blank');
		}
			  });
		}
		
	},


	bindSearch:function(){

		$(document).on("submit",'#storeFinderForm', function(e){
			e.preventDefault();
			var q = $(".js-asm-store-finder-search-input").val();

			if(q.length>0){
				ASM.storefinder.getInitStoreData(q);
			}else{
				if($(".js-asm-storefinder-alert").length<1){
					var emptySearchMessage = $(".btn-primary").data("searchEmpty");
					$(".js-asm-store-finder").hide();
					$("#storeFinder").before('<div class="js-asm-storefinder-alert alert alert-danger alert-dismissable getAccAlert" ><button class="close closeAccAlert" type="button" data-dismiss="alert" aria-hidden="true">×</button>' + emptySearchMessage + '</div>');
				}
			}
		});


		$(".js-asm-store-finder").hide();
		$(document).on("click",'#findStoresNearMe', function(e){
			e.preventDefault();
			ASM.storefinder.getInitStoreData(null,ASM.storefinder.coords.latitude, ASM.storefinder.coords.longitude);
		});


	},


	getStoreData: function(page){
		ASM.storefinder.storeSearchData.page = page;
		url= $(".js-asm-store-finder").data("url");
		$.ajax({
			url: url,
			data: ASM.storefinder.storeSearchData,
			type: "get",
			success: function (response){
				ASM.storefinder.storeData = $.parseJSON(response);
				ASM.storefinder.refreshNavigation();
				if(ASM.storefinder.storeData.total < 10){
					$(".js-asm-store-finder-pager-next").attr("disabled","disabled");
				}
			}
		});
	},

	getInitStoreData: function(q,latitude,longitude){
		$(".alert").remove();
		data ={
			"q":"" ,
			"page":0
		};
		if(q != null){
			data.q = q;
		}

		if(latitude != null){
			data.latitude = latitude;
		}

		if(longitude != null){
			data.longitude = longitude;
		}

		ASM.storefinder.storeSearchData = data;
		ASM.storefinder.getStoreData(data.page);
		$(".js-asm-store-finder").show();
		$(".js-asm-store-finder-pager-prev").attr("disabled","disabled");
		$(".js-asm-store-finder-pager-next").removeAttr("disabled");
	},

	init:function(){
		$("#findStoresNearMe").attr("disabled","disabled");
		if(navigator.geolocation){
			navigator.geolocation.getCurrentPosition(
				function (position){
					ASM.storefinder.coords = position.coords;
					$('#findStoresNearMe').removeAttr("disabled");
				},
				function (error)
				{
					console.log("An error occurred... The error code and message are: " + error.code + "/" + error.message);
				}
			);
		}
	}
};
