<html>
<head>
    <title>index测试页面</title>
</head>
<body>
<table border=1px; >
    <tr>
		<th colspan=6>index测试页面!!!</th>
          <br/>
		<span>
          图片编码格式:
          <img width=100 id="imageId"  height=100 src="data:image/gif;base64,"/>
          <br/>
          图片连接格式:
          <img width=100 height=100 src="http://www.jiqie.com/i/55/1656.gif?053111344330908"/>
          <br/>
          图片流格式:
          <img width=100 height=100 src="file/img2"/>
        </span>
    </tr>
</table>


</body>

<script type="text/javascript">

    function ajax(){ 
          var ajaxData = { 
            type:arguments[0].type || "GET", 
            url:arguments[0].url || "", 
            async:arguments[0].async || "true", 
            data:arguments[0].data || null, 
            dataType:arguments[0].dataType || "text", 
            contentType:arguments[0].contentType || "application/x-www-form-urlencoded", 
            beforeSend:arguments[0].beforeSend || function(){}, 
            success:arguments[0].success || function(){}, 
            error:arguments[0].error || function(){} 
          } 
          ajaxData.beforeSend() 
          var xhr = createxmlHttpRequest();  
          xhr.responseType=ajaxData.dataType; 
          xhr.open(ajaxData.type,ajaxData.url,ajaxData.async);  
          xhr.setRequestHeader("Content-Type",ajaxData.contentType);  
          xhr.send(convertData(ajaxData.data));  
          xhr.onreadystatechange = function() {  
            if (xhr.readyState == 4) {  
              if(xhr.status == 200){ 
                ajaxData.success(xhr.response) 
              }else{ 
                ajaxData.error() 
              }  
            } 
          }  
        } 
          
        function createxmlHttpRequest() {  
          if (window.ActiveXObject) {  
            return new ActiveXObject("Microsoft.XMLHTTP");  
          } else if (window.XMLHttpRequest) {  
            return new XMLHttpRequest();  
          }  
        } 
          
        function convertData(data){ 
          if( typeof data === 'object' ){ 
            var convertResult = "" ;  
            for(var c in data){  
              convertResult+= c + "=" + data[c] + "&";  
            }  
            convertResult=convertResult.substring(0,convertResult.length-1) 
            return convertResult; 
          }else{ 
            return data; 
          } 
        }
    
     /*用window.onload调用myfun()*/　
　function onloadfun() { 　　
       //alert("this window.onload"); 　　
       ajax({ 
          type:"POST", 
          url:"file/img1", 
          dataType:"json", 
          data:{"key1":"abc"}, 
          beforeSend:function(){ 
            //some js code 
          }, 
          success:function(msg){ 
            console.log(msg) 
            if(msg.code === "0"){
                //alert(JSON.stringify(msg.data.image)); 　　
                document.getElementById("imageId").src = "data:image/gif;base64,"+msg.data.image;
            }else{
                alert(JSON.stringify(msg.message)); 　　
            }
          }, 
          error:function(){ 
            console.log("error") 
          } 
        });

    } 　　
            　
    //不要括号
    window.onload = onloadfun;
        
</script>

</html>