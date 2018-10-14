<template>
<div class="login-box">
    <el-row>
        <el-col :span="8">
            <el-input id="account"  v-model="account" placeholder="请输入账号">
                <template slot="prepend">账&nbsp&nbsp&nbsp&nbsp号</template>
            </el-input>
        </el-col>
    </el-row>
    <el-row>
        <el-col :span="8">
            <el-input id="password" v-model="password" type="password" placeholder="请输入密码">
                <template slot="prepend">密&nbsp&nbsp&nbsp&nbsp码</template>
            </el-input>
        </el-col>
    </el-row>
    <el-row>
        <el-col :span="8">
            <el-input id="name" v-model="name" placeholder="请输入用户名">
                <template slot="prepend">用户名</template>
            </el-input>
        </el-col>
    </el-row>
    <el-row>
        <el-col :span="8">
            <el-button id="login" v-on:click="check" style="width:100%" type="primary">确定</el-button>
        </el-col>
    </el-row>
</div>
</template>

<script>
export default {
    name: 'register',
    data () {
        return {
            account : '',
            password : '',
            name:'',
        }
    },
    methods : {
            check : function(event){
                //获取值
                var name = this.name;
                var password = this.password;
                var account = this.account;
                if(name == '' || password == '' || account == ''){
                    this.$message({
                        message : '不能为空！',
                        type : 'error'
                    })
                    return;
                }
                axios.defaults.withCredentials = true
                axios({
                    method:'post',
                    url:'http://localhost:8080/api/user/register',
                    data:{
                        account:account,
                        password:password,
                        name:name,
                    },
                    transformRequest: [function (data, header) {
                        var result = "account=" + account + "&password=" + password + "&name=" + name
                        return result;
                    }]
                }).then(function(response) {
                    alert(response.data.message)
                    if(response.data.success){
                        window.location.href="./select.html";
                    }
                }.bind(this));
            }
        }
}
</script>

<style scoped>
.el-row {
            margin-bottom: 20px;
        &:last-child {
             margin-bottom: 0;
         }
        }
        .login-box {
            margin-top:20%;
            margin-left:40%;
        }
</style>
