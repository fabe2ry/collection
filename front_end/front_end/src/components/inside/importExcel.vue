<template>
<div>
	<div>
		<!--<form action="http://localhost:8080/api/user/importGoodsExcel" enctype="multipart/form-data" method="post">-->
		<!--<input type="file" name="file"/>-->
		<!--<input type="submit" value="提交excel文件">-->
		<!--</form>-->
		<h1>上传Excel</h1>
		<input type="file" name="file" @change="onExcelFileChange" />
		<button type="button" class="btn btn btn-primary" v-on:click="uploadExcelWithDirectReturn">上传(直接返回excel文件)</button>
		<button type="button" class="btn btn btn-primary" v-on:click="uploadExcelWithReturn">上传(不直接返回excel)</button>
		<!--unfinished-->
		<!--<form @submit.prevent="submit">-->
		<!--<input type="file" name="file" v-model="myExcelFile">-->
		<!--<input type="submit" value="提交">-->
		<!--</form>-->

		<br />
		<br />
		<br />

		<button type="button" class="btn btn btn-primary" v-on:click="downloadExcel">下载随机数据模板</button>

		<br />
		<br />
		<br />

		<h1>上传图片</h1>
		<input type="file" name="file" @change="onImgFileChange" />
		<button type="button" class="btn btn btn-primary" v-on:click="uploadImg">上传</button>

		<br />
		<br />
		<br />

		<button type="button" class="btn btn btn-primary" v-on:click="downloadImg">下载上传图片</button>

		<br />
		<br />
		<br />
		<button type="button" class="btn btn btn-primary" v-on:click="showImg">展示图片</button>

		<br />
		<br />
		<br />
		<img :src="mySrc" />
	</div>

	<!-- 模态框（Modal） -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">下载错误文件</h4>
				</div>
				<div class="modal-body">
					<a :href="myErrorLink">下载错误文件</a>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal -->
	</div>
</div>
</template>

<script>
export default {
	name: 'importExcel',
	data() {
		return {
			mySrc: 'http://placehold.it/200x100/0f0/ccc.png',
			myExcelFile: '',
			myImgFile: '',
			myErrorLink: 'www.baidu.com',
		}
	},
	watch: {},
	mounted: function() {},
	methods: {
		downloadExcel: function() {
			window.location.href = "http://localhost:8080/api/user/getTestExcel";
		},
		downloadImg: function() {
			this.axios.defaults.withCredentials = true
			this.axios({
				method: 'get',
				url: 'http://localhost:8080/api/user/imgDownload',
				//                    responseType:'blob',
			}).then(function(response) {
				//                    var url = window.URL.createObjectURL(response.data)
				//                    var link = document.createElement('a')
				//                    link.style.display = 'none'
				//                    link.href = url
				//                    link.setAttribute('download', 'my.png')
				//                    document.body.appendChild(link)
				//                    link.click()
				//                    console.log(url)

				if (response.data.success) {
					window.location.href = response.data.result

					//                        var link = document.createElement('a')
					//                        link.style.display = 'none'
					//                        link.href = response.data.result
					//                        link.setAttribute('download', 'download.png')
					//                        document.body.appendChild(link)
					//                        link.click()
				} else {
					alert(response.data.message)
				}
			}.bind(this));
		},
		showImg: function() {
			this.axios.defaults.withCredentials = true
			this.axios({
				method: 'get',
				url: 'http://localhost:8080/api/user/imgShow',
			}).then(function(response) {
				if (response.data.success) {
					this.mySrc = response.data.result
				} else {
					alert(response.data.message)
				}
			}.bind(this));
		},
		onExcelFileChange: function(event) {
			console.log(event)
			console.log(event.target)
			console.log(event.target.files)
			console.log(event.target.files[0])
			this.myExcelFile = event.target.files[0]
		},
		uploadExcelWithReturn: function() {
			if (typeof this.myExcelFile != "object") {
				alert("请选择文件")
				return
			}

			var formdata = new FormData();
			formdata.append("file", this.myExcelFile)
			var config = {
				headers: {
					'Content-Type': 'multipart/form-data'
				}
			};;

			this.axios.defaults.withCredentials = true

			this.axios.post('http://localhost:8080/api/user/importGoodsExcelWithReturn', formdata, config)
				.then(function(response) {
					if (response.data.success) {
						alert(response.data.message)
					} else {
						alert(response.data.message)
						if (response.data.result != null) {
							this.myErrorLink = response.data.result;
							$("#myModal").modal('show');
						}
					}
				}.bind(this))
		},
		uploadExcelWithDirectReturn: function() {
			if (typeof this.myExcelFile != "object") {
				alert("请选择文件")
				return
			}

			var formdata = new FormData();
			formdata.append("file", this.myExcelFile)
			var config = {
				headers: {
					'Content-Type': 'multipart/form-data'
				}
			};;

			this.axios.defaults.withCredentials = true
			//                this.axios({
			//                    method:'post',
			//                    url:'http://localhost:8080/api/user/imgUplaod',
			//                    formdata:formdata,
			//                    headers :{
			//                        'Content-Type':'multipart/form-data'
			//                    }
			//                }).then(function(response) {
			//                })
			this.axios.post('http://localhost:8080/api/user/importGoodsExcelWithDirectReturn', formdata, config, {
					responseType: 'arraybuffer'
				})
				.then(function(response) {
					var returnType = response.headers['content-type']
					if (returnType.indexOf('json') != -1) {
						if (response.data.success) {
							alert(response.data.message)
						} else {
							alert(response.data.message)
						}
					} else if (returnType.indexOf('vnd') != -1) {
						var blob = new Blob([response.data], {
							type: returnType
						})
						var url = window.URL.createObjectURL(blob)
						var link = document.createElement('a')
						link.style.display = 'none'
						link.href = url
						link.setAttribute('download', "download.xlsx")
						document.body.appendChild(link)
						link.click()
					}
				})
		},
		//            submit:function (event) {
		//                console.log(event)
		//                console.log(event.target.file)
		//            },
		onImgFileChange: function(event) {
			this.myImgFile = event.target.files[0]
		},
		uploadImg: function() {
			if (typeof this.myImgFile != "object") {
				alert("请选择文件")
				return
			}

			var formdata = new FormData();
			formdata.append("file", this.myImgFile)

			var config = {
				headers: {
					'Content-Type': 'multipart/form-data'
				}
			};;

			this.axios.defaults.withCredentials = true
			this.axios.post('http://localhost:8080/api/user/imgUplaod', formdata, config)
				.then(function(response) {
					if (response.data.success) {
						alert('上传成功 图片链接：' + response.data.result + ' 你也可以点击按钮[展示图片]查看上传的图片')
					} else {
						alert(response.data.message)
					}
				})
		}
	}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
