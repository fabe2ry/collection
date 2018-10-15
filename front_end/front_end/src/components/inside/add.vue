<template>
<div>
	<div class="col-md-12 column">
		<div class="form-group">
			<label for="">account</label><input v-model="account" type="" class="form-control" />
		</div>
		<div class="form-group">
			<label for="">password</label><input v-model="password" type="password" class="form-control" />
		</div>
		<div class="form-group">
			<label for="">name</label><input v-model="name" type="" class="form-control" />
		</div>
		<div class="form-group">
			<label for="">privilege</label><input v-model="privilege" type="" class="form-control" />
		</div>
		<button class="btn btn-default" v-on:click="check">提交</button>
	</div>
</div>
</template>

<script>
export default {
	name: 'add',
	data() {
		return {
			account: '',
			name: '',
			password: '',
			privilege: ''
		}
	},
	methods: {
		check: function(event) {
			//获取值
			var account = this.account;
			var password = this.password;
			var name = this.name;
			var privilege = this.privilege;
			if (name == '' || name.length == 0 ||
				password == '' || password.length == 0 ||
				account == "" || account.length == 0 ||
				privilege == "" || privilege.length == 0) {
				alert("账号或密码为空")
				return;
			}
			this.axios.defaults.withCredentials = true
			this.axios({
				method: 'post',
				url: 'http://localhost:8080/api/user/add',
				data: {
					account: account,
					password: password,
					name: name,
					privilege: privilege
				},
				transformRequest: [function(data, header) {
					var result = "account=" + name + "&password=" + password + "&name=" + name + "&privilege=" + privilege
					return result;
				}]
			}).then(function(response) {
				alert(response.data.message)
				if (response.data.success) {
					this.$router.push('/hall/select')
				}
			})
		}
	}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
