<template>
  <div>
    <div style="height: 70px;width:auto;background-color: #d8d1cc">
      <div class="header">
        <div class="header-left" style="margin-left: 15px">
          <img src="@/assets/imgs/logo.png" />
          <div class="title">助农扶贫网</div>
        </div>
        <div class="header-right">
          <span class="title">登录中心</span>
        </div>
      <div style="flex: 1"></div>
      </div>
    </div>
    <div class="container">
      <div style="width: 400px; padding: 30px; background-color: white; border-radius: 5px; opacity: 0.9">
        <div style="text-align: center; font-size: 20px; margin-bottom: 20px; color: #11549a; font-weight: bold;">欢迎登录扶贫信息网</div>
        <el-form :model="form" :rules="rules" ref="formRef">
          <el-form-item prop="username">
            <el-input prefix-icon="el-icon-user" placeholder="请输入账号" v-model="form.username"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input prefix-icon="el-icon-lock" placeholder="请输入密码" show-password  v-model="form.password"></el-input>
          </el-form-item>
          <!--        <el-form-item>-->
          <!--          <el-select v-model="form.role" placeholder="请选择角色" style="width: 100%">-->
          <!--            <el-option label="管理员" value="ADMIN"></el-option> &lt;!&ndash;要和RoleEnum类创建的两个对象名称一致&ndash;&gt;-->
          <!--            <el-option label="用户" value="USER"></el-option>-->
          <!--          </el-select>-->
          <!--        </el-form-item>-->
          <el-form-item>
            <el-button style="width: 100%; background-color: #11549a; border-color: #28826b; color: white; font-weight: bold" @click="login">登 录</el-button>
          </el-form-item>
          <div style="display: flex; align-items: center">
            <div style="float:left;text-align: left">
              <a href="/">返回主页</a>
            </div>
            <div style="flex: 1"></div>
            <div style="text-align: right">
              还没有账号？请注册  <!--<a href="/register">注册</a>-->
            </div>
          </div>
        </el-form>
      </div>
    </div>
    <div class="footer">
      <div class="footerLeft">
        Copyrights xxx | All Rights Reserved
      </div>
      <div style="flex: 1"></div>
      <div class="footerRight">
        备案号:xxxxxxx
      </div>
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

      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          // 验证通过
          this.$request.post('/login', this.form).then(res => {  /*from里面有登录所需的账户、密码和角色三个字段属性*/
            if (res.code === '200') {
              localStorage.setItem("xm-user", JSON.stringify(res.data))  // 存储用户数据
              if (res.data.role === 'ADMIN') {
                this.$router.push('/admin/home')  // 跳转主页
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
  height: 84vh;
  /*overflow: hidden;*/
  background-image: url("@/assets/imgs/bg2.jpg");
  background-size: 100%;
  display: flex;
  padding-right: 40px;
  align-items: center;
  /*justify-content: center;*/
  justify-content: flex-end;
  color: #666;
}
a {
  color: #2a60c9;
}
.header{
    display: flex;
    background-color: white;
}
.header-left {
  width: 200px;
  height: 70px;
  padding-left: 10px;
  display: flex;
  align-items: center
}

.header-left img {
  width: 40px;
  height: 40px;
  border-radius: 5px;
}
.header-left .title {
  flex: 1;
  margin-left: 5px;
  font-size: 24px;
  font-weight: bold;
  color: #5c6c82;
}

.header-right {
  width: 200px;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  font-size: 15px;

  color: #5c6c82;
}

.footer{
  height: 50px;
  display: flex;
  background-color: white;
}

.footerLeft{
  margin-top: 15px;
  margin-left: 40px;
  font-size: 15px;
  color: #5c6c82;
  align-items: center;
  justify-content: flex-start;
}

.footerRight{
  margin-top: 15px;
  margin-right: 80px;
  font-size: 15px;
  color: #5c6c82;
  align-items: center;
  justify-content: flex-end;
}
</style>