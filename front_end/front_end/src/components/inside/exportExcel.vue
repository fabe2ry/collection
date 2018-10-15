<template>
<div>
	<div>
		<button type="button" class="btn btn btn-primary" v-on:click="download">下载</button>
		<template>
			<table class="table table-bordered">
				<thead>
					<tr class="info">
						<th v-for="head in myHeads">
							{{head}}
						</th>
					</tr>
				</thead>
				<tbody>
					<!-- 当数据为空时 -->
					<template v-if='myBodys.length == 0'>
						<tr>
							<td :colspan="myHeads.length + 2" align="center">无&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp数&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp据</td>
						</tr>
					</template>
					<template v-else>
						<tr v-for="body in myBodys">
							<template>
								<td v-for="value in body">{{value}}</td>
							</template>
						</tr>
					</template>
				</tbody>
			</table>
		</template>
		<el-pagination background layout="prev, pager, next" :current-page="myPageIndex" :page-size="myPageSize" :total="myPageCount" v-on:current-change="clickCurrentPage">
		</el-pagination>
	</div>
</div>
</template>

<script>
export default {
	name: 'exportExcel',
	data() {
		return {
			myHeads: ['id', 'name', 'type'],
			myBodys: [],

			myPageSize: 5,
			myPageCount: 5,
			myPageIndex: 0,
		}
	},
	watch: {
		myBodys: function() {}
	},
	mounted: function() {
		this.getPage(0, 5)
	},
	methods: {
		download: function() {
			window.location.href = "http://localhost:8080/api/user/exportGoodsExcel";
		},
		getPage: function(pageIndex, pageSize) {
			this.axios.defaults.withCredentials = true
			this.axios({
				method: 'get',
				url: 'http://localhost:8080/api/user/getGoods/' + pageIndex + '/' + pageSize,
			}).then(function(response) {
				if (response.data.success) {
					this.myBodys = response.data.result
					this.myPageCount = response.data.total
				} else {
					alert(response.data.message)
				}
			}.bind(this));
		},
		clickCurrentPage: function(currentPage) {
			this.myPageIndex = currentPage;
			this.getPage(currentPage, this.myPageSize)
		},
	}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
