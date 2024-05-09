<template>
  <div class="container">
    <div style="width: 400px; padding: 30px; background-color: white; border-radius: 5px; opacity: 0.9">
      <div style="text-align: center; font-size: 20px; margin-bottom: 20px; color: #11549a; font-weight: bold;">欢迎登录助农扶贫网</div>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input prefix-icon="el-icon-user" placeholder="请输入账号" v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" placeholder="请输入密码" show-password  v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-select v-model="form.role" placeholder="请选择角色" style="width: 100%">
            <el-option label="管理员" value="ADMIN"></el-option> <!--要和RoleEnum类创建的两个对象名称一致-->
            <el-option label="用户" value="USER"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%; background-color: #11549a; border-color: #28826b; color: white; font-weight: bold" @click="login">登 录</el-button>
        </el-form-item>
        <div style="display: flex; align-items: center">
          <div style="flex: 1"></div>
          <div style="flex: 1; text-align: right">
            还没有账号？请 <a href="/register">注册</a>
          </div>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      //form: { role: 'ADMIN' }, 默认下拉框内容为管理员
      form: {},
      dialogVisible: true,
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ]
      }
    }
  },
  created() {

  },
  methods: {
    login() {  /*点击登录按钮后，就运行登录login函数；login函数先去验证表单，然后调用接口，把form传过去*/
      //ES6允许使用“箭头”（=>）定义函数
      /*var f = a = > a等同于
        var f = function(a){
          return a;
        }
       **************************
       如果箭头函数不需要参数或需要多个参数，就使用一个圆括号代表参数部分。
       *******************无形参*******************
        var f = () => 5;等同于var f = function () { return 5 };
       *******************多个形参*******************
        var sum = (num1, num2) => num1 + num2;等同于
        var sum = function(num1, num2) {
            return num1 + num2;
         };
      */
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          // 验证通过
          this.$request.post('/login', this.form).then(res => {  /*from里面有登录所需的账户、密码和角色三个字段属性*/
            if (res.code === '200') {
              localStorage.setItem("xm-user", JSON.stringify(res.data))  // 存储用户数据
              if (res.data.role === 'ADMIN') {
                this.$router.push('/home')  // 跳转主页
              } else {
                location.href = '/front/home'
              }
              this.$message.success('登录成功')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  height: 100vh;
  overflow: hidden;
  background-image: url("@/assets/imgs/bg2.jpg");
  background-size: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
}
a {
  color: #2a60c9;
}
</style>