<template>
    <div class="login-box">
        <el-row>
            <el-col :span="8">
                <el-input id="name"  v-model="name" placeholder="请输入帐号">
                    <template slot="prepend">帐号</template>
                </el-input>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="8">
                <el-input id="password" v-model="password" type="password" placeholder="请输入密码">
                    <template slot="prepend">密码</template>
                </el-input>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="8">
                <el-button id="login" v-on:click="check" style="width:100%" type="primary">登录</el-button>

            </el-col>
        </el-row>
        <el-row>
            <el-col :span="8">
                <el-button id="register" v-on:click="register" style="width:100%">注册</el-button>
            </el-col>
        </el-row>
    </div>
</template>

<script>
export default {
    name: 'login',
    data () {
        return {
            name : '',
            password : ''
        }
    },
    methods : {
        check : function(event){
            //获取值
            var name = this.name;
            var password = this.password;
            if(name == '' || password == ''){
                this.$message({
                    message : '账号或密码为空！',
                    type : 'error'
                })
                return;
            }

            axios.defaults.withCredentials = true
            axios({
                method:'post',
                url:'http://localhost:8080/api/user/login',
                data:{
                    account:name,
                    password:password
                },
                transformRequest: [function (data, header) {
                    var result = "account=" + name + "&password=" + password
                    return result;
                }]
            }).then(function(response) {
                alert(response.data.message)
                if(response.data.success){
                    window.location.href="./select";
                }
            })
        },
        register:function (event) {
            window.location.href="./register";
            // this.$router.push('/register')
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
