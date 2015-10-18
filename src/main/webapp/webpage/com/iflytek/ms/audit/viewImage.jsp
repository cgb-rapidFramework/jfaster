<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<title>${url}</title> <t:base type="jquery,easyui,tools,DatePicker"></t:base>
	<link href="plug-in/imageview/css/style.css" rel="stylesheet"
		type="text/css">
		<style>
#idContainer {
	BORDER-BOTTOM: #000 1px solid;
	BORDER-LEFT: #000 1px solid;
	WIDTH: 580px;
	BACKGROUND: #fff no-repeat center 100%;
	HEIGHT: 470px;
	BORDER-TOP: #000 1px solid;
	BORDER-RIGHT: #000 1px solid;	
}

.div_input20 {
	margin: 5px 0 0 0;
	width: 598px;
	height: auto;
	float: left;
}

.div_input20 input {
	background: url(plug-in/imageview/images/but_img.png) no-repeat;
	width: 79px;
	height: 38px;
	border: 0;
	font: Arial, Helvetica, sans-serif "Lucida Console", Monaco, monospace,
		"宋体";
	font-size: 12px;
	color: #fff;
	filter: DropShadow(Color =   #000, OffX =   0, OffY =   1, Positive =   1);
	cursor: pointer;
}

img {position absolute;border 0px none;
	padding: 0px;
	margin: 0px;
	width: auto;
	height: auto;
	visibility: visible;
	top: 0;
	left: 0;
	transform: matrix(1, 0, 0, 1, 0, 0);
}

.div_line {
	border-bottom: 1px solid #808185;
	width: 100%;
	height: 1px;
	margin: 20px auto;
}
</style>
</head>
<body bgcolor="#fff">
	<script src="plug-in/imageview/js/CJL.js"></script>
	<script>
		//容器对象
		var ImageTrans = function(container, options) {
			this._initialize(container, options);
			this._initMode();
			if (this._support) {
				this._initContainer();
				this._init();
			} else {//模式不支持
				this.onError("not support");
			}
		};
		ImageTrans.prototype = {
			//初始化程序
			_initialize : function(container, options) {
				var container = this._container = $$(container);
				this._clientWidth = container.clientWidth;//变换区域宽度
				this._clientHeight = container.clientHeight;//变换区域高度
				this._img = new Image();//图片对象
				this._style = {};//备份样式
				this._x = this._y = 1;//水平/垂直变换参数
				this._radian = 0;//旋转变换参数
				this._support = false;//是否支持变换
				this._init = this._load = this._show = this._dispose = $$.emptyFunction;

				var opt = this._setOptions(options);

				this._zoom = opt.zoom;

				this.onPreLoad = opt.onPreLoad;
				this.onLoad = opt.onLoad;
				this.onError = opt.onError;

				this._LOAD = $$F.bind(function() {
					this.onLoad();
					this._load();
					this.reset();
					this._img.style.visibility = "visible";
				}, this);

				$$CE.fireEvent(this, "init");
			},
			//设置默认属性
			_setOptions : function(options) {
				this.options = {//默认值
					mode : "css3|filter|canvas",
					zoom : .1,//缩放比率
					onPreLoad : function() {
					},//图片加载前执行
					onLoad : function() {
					},//图片加载后执行
					onError : function(err) {
					}//出错时执行
				};
				return $$.extend(this.options, options || {});
			},
			//模式设置
			_initMode : function() {
				var modes = ImageTrans.modes;
				this._support = $$A.some(this.options.mode.toLowerCase().split(
						"|"), function(mode) {
					mode = modes[mode];
					if (mode && mode.support) {
						mode.init && (this._init = mode.init);//初始化执行程序
						mode.load && (this._load = mode.load);//加载图片执行程序
						mode.show && (this._show = mode.show);//变换显示程序
						mode.dispose && (this._dispose = mode.dispose);//销毁程序
						//扩展变换方法
						$$A.forEach(ImageTrans.transforms,
								function(transform, name) {
									this[name] = function() {
										transform.apply(this, [].slice
												.call(arguments));
										this._show();
									}
								}, this);
						return true;
					}
				}, this);
			},
			//初始化容器对象
			_initContainer : function() {
				var container = this._container, style = container.style, position = $$D
						.getStyle(container, "position");
				this._style = {
					"position" : style.position,
					"overflow" : style.overflow
				};//备份样式
				if (position != "relative" && position != "absolute") {
					style.position = "relative";
				}
				style.overflow = "hidden";
				$$CE.fireEvent(this, "initContainer");
			},
			//加载图片
			load : function(src) {
				if (this._support) {
					var img = this._img, oThis = this;
					img.onload || (img.onload = this._LOAD);
					img.onerror || (img.onerror = function() {
						oThis.onError("err image");
					});
					img.style.visibility = "hidden";
					this.onPreLoad();
					img.src = src;
				}
			},
			//重置
			reset : function() {
				if (this._support) {
					this._x = this._y = 1;
					this._radian = 0;
					this._show();
				}
			},
			//销毁程序
			dispose : function() {
				if (this._support) {
					this._dispose();
					$$CE.fireEvent(this, "dispose");
					$$D.setStyle(this._container, this._style);//恢复样式
					this._container = this._img = this._img.onload = this._img.onerror = this._LOAD = null;
				}
			}
		};
		//变换模式
		ImageTrans.modes = function() {
			var css3Transform;//ccs3变换样式
			//初始化图片对象函数
			function initImg(img, container) {
				$$D.setStyle(img, {
					position : "absolute",
					border : 0,
					padding : 0,
					margin : 0,
					width : "auto",
					height : "auto",//重置样式
					visibility : "hidden"//加载前隐藏
				});
				container.appendChild(img);
			}
			//获取变换参数函数
			function getMatrix(radian, x, y) {
				var Cos = Math.cos(radian), Sin = Math.sin(radian);
				return {
					M11 : Cos * x,
					M12 : -Sin * y,
					M21 : Sin * x,
					M22 : Cos * y
				};
			}
			return {
				css3 : {//css3设置
					support : function() {
						var style = document.createElement("div").style;
						return $$A.some([ "transform", "MozTransform",
								"webkitTransform", "OTransform" ],
								function(css) {
									if (css in style) {
										css3Transform = css;
										return true;
									}
								});
					}(),
					init : function() {
						initImg(this._img, this._container);
					},
					load : function() {
						var img = this._img;
						/* $$D.setStyle(img, {//居中
							top : (this._clientHeight - img.height) / 2 + "px",
							left : (this._clientWidth - img.width) / 2 + "px",
							visibility : "visible"
						}); */
					},
					show : function() {
						var matrix = getMatrix(this._radian, this._y, this._x);
						//设置变形样式
						this._img.style[css3Transform] = "matrix("
								+ matrix.M11.toFixed(16) + ","
								+ matrix.M21.toFixed(16) + ","
								+ matrix.M12.toFixed(16) + ","
								+ matrix.M22.toFixed(16) + ", 0, 0)";
					},
					dispose : function() {
						this._container.removeChild(this._img);
					}
				},
				filter : {//滤镜设置
					support : function() {
						return "filters" in document.createElement("div");
					}(),
					init : function() {
						initImg(this._img, this._container);
						//设置滤镜
						this._img.style.filter = "progid:DXImageTransform.Microsoft.Matrix(SizingMethod='auto expand')";
					},
					load : function() {
						this._img.onload = null;//防止ie重复加载gif的bug
						this._img.style.visibility = "visible";
					},
					show : function() {
						var img = this._img;
						//设置滤镜
						$$.extend(img.filters
								.item("DXImageTransform.Microsoft.Matrix"),
								getMatrix(this._radian, this._y, this._x));
						//保持居中
						img.style.top = (this._clientHeight - img.offsetHeight)
								/ 2 + "px";
						img.style.left = (this._clientWidth - img.offsetWidth)
								/ 2 + "px";
					},
					dispose : function() {
						this._container.removeChild(this._img);
					}
				},
				canvas : {//canvas设置
					support : function() {
						return "getContext" in document.createElement('canvas');
					}(),
					init : function() {
						var canvas = this._canvas = document
								.createElement('canvas'), context = this._context = canvas
								.getContext('2d');
						//样式设置
						$$D.setStyle(canvas, {
							position : "absolute",
							left : 0,
							top : 0
						});
						canvas.width = this._clientWidth;
						canvas.height = this._clientHeight;
						this._container.appendChild(canvas);
					},
					show : function() {
						var img = this._img, context = this._context, clientWidth = this._clientWidth, clientHeight = this._clientHeight;
						//canvas变换
						context.save();
						context.clearRect(0, 0, clientWidth, clientHeight);//清空内容
						context.translate(clientWidth / 2, clientHeight / 2);//中心坐标
						context.rotate(this._radian);//旋转
						context.scale(this._y, this._x);//缩放
						context.drawImage(img, -img.width / 2, -img.height / 2);//居中画图
						context.restore();
					},
					dispose : function() {
						this._container.removeChild(this._canvas);
						this._canvas = this._context = null;
					}
				}
			};
		}();
		//变换方法
		ImageTrans.transforms = {
			//垂直翻转
			vertical : function() {
				this._radian = Math.PI - this._radian;
				this._y *= -1;
			},
			//水平翻转
			horizontal : function() {
				this._radian = Math.PI - this._radian;
				this._x *= -1;
			},
			//根据弧度旋转
			rotate : function(radian) {
				this._radian = radian;
			},
			//向左转90度
			left : function() {
				this._radian -= Math.PI / 2;
			},
			//向右转90度
			right : function() {
				this._radian += Math.PI / 2;
			},
			//根据角度旋转
			rotatebydegress : function(degress) {
				this._radian = degress * Math.PI / 180;
			},
			//缩放
			scale : function() {
				function getZoom(scale, zoom) {
					return scale > 0 && scale > -zoom ? zoom : scale < 0
							&& scale < zoom ? -zoom : 0;
				}
				return function(zoom) {
					if (zoom) {
						var hZoom = getZoom(this._y, zoom), vZoom = getZoom(
								this._x, zoom);
						if (hZoom && vZoom) {
							this._y += hZoom;
							this._x += vZoom;
						}
					}
				}
			}(),
			//放大
			zoomin : function() {
				this.scale(Math.abs(this._zoom));
			},
			//缩小
			zoomout : function() {
				this.scale(-Math.abs(this._zoom));
			}
		};

		//拖动旋转
		ImageTrans.prototype._initialize = (function() {
			var init = ImageTrans.prototype._initialize, methods = {
				"init" : function() {
					this._mrX = this._mrY = this._mrRadian = 0;
					this._mrSTART = $$F.bind(start, this);
					this._mrMOVE = $$F.bind(move, this);
					this._mrSTOP = $$F.bind(stop, this);
				},
				"initContainer" : function() {
					$$E.addEvent(this._container, "mousedown", this._mrSTART);
				},
				"dispose" : function() {
					$$E
							.removeEvent(this._container, "mousedown",
									this._mrSTART);
					this._mrSTOP();
					this._mrSTART = this._mrMOVE = this._mrSTOP = null;
				}
			};
			//开始函数
			function start(e) {
				var rect = $$D.clientRect(this._container);
				this._mrX = rect.left + this._clientWidth / 2;
				this._mrY = rect.top + this._clientHeight / 2;
				this._mrRadian = Math.atan2(e.clientY - this._mrY, e.clientX
						- this._mrX)
						- this._radian;
				$$E.addEvent(document, "mousemove", this._mrMOVE);
				$$E.addEvent(document, "mouseup", this._mrSTOP);
				if ($$B.ie) {
					var container = this._container;
					$$E.addEvent(container, "losecapture", this._mrSTOP);
					container.setCapture();
				} else {
					$$E.addEvent(window, "blur", this._mrSTOP);
					e.preventDefault();
				}
			}
			;
			//拖动函数
			function move(e) {
				this.rotate(Math.atan2(e.clientY - this._mrY, e.clientX
						- this._mrX)
						- this._mrRadian);
				window.getSelection ? window.getSelection().removeAllRanges()
						: document.selection.empty();
			}
			;
			//停止函数
			function stop() {
				$$E.removeEvent(document, "mousemove", this._mrMOVE);
				$$E.removeEvent(document, "mouseup", this._mrSTOP);
				if ($$B.ie) {
					var container = this._container;
					$$E.removeEvent(container, "losecapture", this._mrSTOP);
					container.releaseCapture();
				} else {
					$$E.removeEvent(window, "blur", this._mrSTOP);
				}
				;
			}
			;
			return function() {
				var options = arguments[1];
				if (!options || options.mouseRotate !== false) {
					//扩展钩子
					$$A.forEach(methods, function(method, name) {
						$$CE.addEvent(this, name, method);
					}, this);
				}
				init.apply(this, arguments);
			}
		})();
		//滚轮缩放
		ImageTrans.prototype._initialize = (function() {
			var init = ImageTrans.prototype._initialize, mousewheel = $$B.firefox ? "DOMMouseScroll"
					: "mousewheel", methods = {
				"init" : function() {
					this._mzZoom = $$F.bind(zoom, this);
				},
				"initContainer" : function() {
					$$E.addEvent(this._container, mousewheel, this._mzZoom);
				},
				"dispose" : function() {
					$$E.removeEvent(this._container, mousewheel, this._mzZoom);
					this._mzZoom = null;
				}
			};
			//缩放函数
			function zoom(e) {
				this.scale((e.wheelDelta ? e.wheelDelta / (-120)
						: (e.detail || 0) / 3)
						* Math.abs(this._zoom));
				e.preventDefault();
			}
			;
			return function() {
				var options = arguments[1];
				if (!options || options.mouseZoom !== false) {
					//扩展钩子
					$$A.forEach(methods, function(method, name) {
						$$CE.addEvent(this, name, method);
					}, this);
				}
				init.apply(this, arguments);
			}
		})();
	</script>

	<div id="idContainer"
		style="position: relative; overflow:hidden; text-align:center;">
	</div>
	<div class="div_input20">
		<%--<input id="idLeft" value="向左旋转" type="button"> 
		<input id="idRight" value="向右旋转" type="button"> 
		<input id="idVertical" value="垂直翻转" type="button"> 
		<input id="idHorizontal" value="水平翻转" type="button"> 
		<input id="idReset" value="重置" type="button">
		--%><input id="idUp" value="上一张" type="button">
		<input id="idDown" value="下一张" type="button">
	</div>
						<div class="div_line"></div>
<script>
			var url = "${url}";
            var i ="${i}";
			var strs = new Array();
			strs = url.split(",");
		(function() {
			if (strs.length < 1) {
				alert("无上传照片");
			} else {
                    if(i!=""){
                        var container = $$("idContainer"), src = strs[i], options = {
                        onPreLoad : function() {
                            container.style.backgroundImage = "url(" + strs[i] + ")";
                        },
                        onLoad : function() {
                            container.style.backgroundImage = "";
                        },
                        onError : function(err) {
                            container.style.backgroundImage = "";
                            ;
                        }
                    }, it = new ImageTrans(container, options);
                    it.load(src);
                }else{
                    var container = $$("idContainer"), src = strs[0], options = {
                        onPreLoad : function() {
                            container.style.backgroundImage = "url(" + strs[0] + ")";
                        },
                        onLoad : function() {
                            container.style.backgroundImage = "";
                        },
                        onError : function(err) {
                            container.style.backgroundImage = "";
                            ;
                        }
                    }, it = new ImageTrans(container, options);
                    it.load(src);
                }
			}
			//垂直翻转
			$$("idUp").onclick = function() {
				Up();
			}
			//垂直翻转
			$$("idDown").onclick = function() {
				down();
			}
			//垂直翻转
		//	$$("idVertical").onclick = function() {
			//	it.vertical();
		//	}
			//水平翻转
		//	$$("idHorizontal").onclick = function() {
		//		it.horizontal();
		//	}
			//左旋转
		//	$$("idLeft").onclick = function() {
		//		it.left();
		//	}
			//右旋转
		//	$$("idRight").onclick = function() {
		//		it.right();
		//	}
			//重置
		//	$$("idReset").onclick = function() {
		//		it.reset();
		//	}
			//换图
			//$$("idLoad").onclick = function(){ it.load( $$("idSrc").value ); }
			//Canvas
			//$$("idCanvas").onclick = function(){
			//if(this.value == "默认模式"){
			//this.value = "使用Canvas"; delete options.mode;
			//}else{
			/// this.value = "默认模式"; options.mode = "canvas";
			//}
			//it.dispose();
			//it = new ImageTrans( container, options );
			//it.load(src);
			//}
		})();
			function page(url) {
				$('#idContainer').empty();
				var container = $$("idContainer"), src = url, options = {
					onPreLoad : function() {
						container.style.backgroundImage = "url(" + url + ")";
					},
					onLoad : function() {
						container.style.backgroundImage = "";
					},
					onError : function(err) {
						container.style.backgroundImage = "";
						;
					}
				}, it = new ImageTrans(container, options);
				it.load(src);
			}
            if(i==undefined||i==""||i==null){
			    var index = 1;
            }else{
                var index = parseInt(i)+1;
            }
			function Up(url) {
				index--;
				if (index <=0) {
					index++;
					alert("已经是第一张");
				} else {
					page(strs[index-1]);
				}
			}
			function down(url) {
				index++;
				if (index > strs.length) {
					index--;
					alert("已经是最后一张");
				} else {
					page(strs[index-1]);
				}
			}
</script>
</body>
</html>