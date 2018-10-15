<template>
<div>
	<el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect" background-color="#545c64" text-color="#fff" active-text-color="#ffd04b">
		<el-menu-item index="1">查看用户</el-menu-item>
		<el-menu-item index="2">添加用户</el-menu-item>
		<el-menu-item index="3">查看日志</el-menu-item>
		<el-menu-item index="4">导入excel</el-menu-item>
		<el-menu-item index="5">导出excel</el-menu-item>
	</el-menu>

	<router-view/>
</div>
</template>

<script>
export default {
	name: 'hall',
	data() {
		return {
			activeIndex: "1",
			// TODO:这样写链接也不是很好
			urlMap: [
				'./select',
				'./add',
				'./vlog',
				'./import',
				'./export'
			]
		}
	},
	mounted:function(){
		this.axios.interceptors.response.use(function (response){
			return response;
		}, function (error) {
			this.$router.push('/logout')
			return Promise.reject(error);
		}.bind(this));
	},
	methods: {
		handleSelect(key, keyPath) {
 			this.activeIndex = key;
			this.$router.push(this.urlMap[key - 1])
		},
	}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
