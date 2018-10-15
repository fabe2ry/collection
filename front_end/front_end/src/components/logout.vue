<template>
<div>

</div>
</template>

<script>
export default {
	name: 'logout',
	data() {
		return {}
	},
	mounted: function() {
		this.logout()
	},
	methods: {
		logout: function() {
			this.axios.defaults.withCredentials = true
			this.axios({
				method: 'get',
				url: 'http://localhost:8080/api/user/logout',
			}).then(function(response) {
				alert(response.data.message)
                this.$router.push('/login')
			}.bind(this))
		},
	}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
this.axios
