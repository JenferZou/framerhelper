<template>
  <div class="main-content">
    <el-card style="width: 50%; margin: 30px auto">
      <div style="text-align: right; margin-bottom: 20px">
        <el-button type="primary" @click="updatePassword">修改密码</el-button>
        <el-button type="warning" @click="rechargeInit">充 值</el-button>  <!--类型warning  方法rechargeInit充值初始化，methods里面具体定义-->
      </div>
      <el-form :model="user" label-width="80px" style="padding-right: 20px">
        <div style="margin: 15px; text-align: center">
<!--        element官方控件，点击会跳出弹窗选择文件
        action调用图片上传接口 $baseUrl定义在main.js中
        由FileController控制层进行处理-->
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/files/upload'"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
          >
            <img v-if="user.avatar" :src="user.avatar" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="user.username" placeholder="用户名" disabled></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="user.name" placeholder="姓名"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="user.phone" placeholder="电话"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="user.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="身份" prop="level">
          <el-input v-model="user.level"  disabled></el-input>
        </el-form-item>
        <el-form-item label="余额" prop="account">
          <el-input v-model="user.account" placeholder="余额" disabled></el-input>
        </el-form-item>
        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="update">保 存</el-button>
        </div>
      </el-form>
    </el-card>

<!--    修改密码对话框-->
    <el-dialog title="修改密码" :visible.sync="dialogVisible" width="30%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="user" label-width="80px" style="padding-right: 20px" :rules="rules" ref="formRef">
        <el-form-item label="原始密码" prop="password">
          <el-input show-password v-model="user.password" placeholder="原始密码"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input show-password v-model="user.newPassword" placeholder="新密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input show-password v-model="user.confirmPassword" placeholder="确认密码"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

<!--    充值 对话框-->
    <el-dialog title="充值信息" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px">
        <el-form-item prop="account" label="充值金额">
          <el-input v-model="account" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="type" label="支付方式">
          <el-radio v-model="type" label="支付宝">支付宝</el-radio>  <!--Element官网找一个 单选框 样式；绑定type-->
          <el-radio v-model="type" label="微信">微信</el-radio>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="recharge">确 定</el-button>  <!--点击确认后使用recharge方法，实现充值操作方法逻辑-->
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
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
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认密码'))
      } else if (value !== this.user.newPassword) {
        callback(new Error('确认密码错误'))
      } else {
        callback()
      }
    }
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),  /*localStorage缓存数据，登录之前缓存*/
      dialogVisible: false,  /*修改密码对话框开关*/
      fromVisible: false,  /*充值 对话框开关*/
      account: null,  /*充值对话框-充值金额定义*/
      type: '支付宝',  /*充值对话框-单选框-默认‘支付宝’选项*/
      rules: {
        password: [
          { required: true, message: '请输入原始密码', trigger: 'blur' },
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
        ],
        confirmPassword: [
          { validator: validatePassword, required: true, trigger: 'blur' },
        ],
      }
    }
  },
  created() {
    this.getPerson()
  },
  methods: {
    /*
    * 从数据库拿数据，不从缓存拿，缓存不太安全
    * 尝试从服务器获取与特定ID关联的用户信息，如果成功则更新本地存储中的用户数据，并在页面上显示一个消息；如果失败，则在页面上显示一个错误消息。
    * */
    getPerson() {
      /*
      * 使用this.$request.get发起一个HTTP GET请求到服务器的/user/selectById/路径，后面跟着当前对象或组件的user.id。这个请求是异步的，所以它返回一个Promise。
      * .then(res => {表示当Promise解决（即请求成功）时，会执行大括号中的回调函数，并将服务器的响应作为参数传递给这个回调函数。
      * */
      this.$request.get('/user/selectById/' + this.user.id).then(res => {
        if (res.code === '200') {
          this.user = res.data  /*如果响应成功，将服务器的响应数据（位于res.data）赋值给当前对象或组件的user属性*/
          localStorage.setItem("xm-user", JSON.stringify(this.user)) /*将更新后的user对象转换为JSON字符串，并使用localStorage.setItem将其存储在浏览器的localStorage中，键名为"xm-user"。*/
        } else {
          this.$message.error(res.msg) /*使用当前对象或组件的$message.error方法显示一个错误消息，消息内容来自服务器的响应中的msg属性。*/
        }
      })
    },

    /*充值初始化方法 */
    rechargeInit() {
      this.account = 100 /*给充值对话框的金额 初始值100*/
      this.fromVisible = true  /*点击充值后 打开对话框*/
    },
    recharge() { /*充值方法逻辑实现：充值只改变余额这个字段 更新user表*/
      this.user.account = parseFloat(this.user.account) + parseFloat(this.account) /*先把余额转成float对象，不然加容易出问题  用户本身的余额+它填写的余额*/
      this.$request.put('/user/update', this.user).then(res => { /*调用后台的方法把user传过去 进行update方法*/
        if (res.code === '200') {
          this.$message.success('充值成功')/*成功提示*/
          localStorage.setItem("xm-user", JSON.stringify(this.user))/*把充值后的数据塞到缓存里面，相当于数据更新*/
//用于使余额实时化显示
// localStorage.setItem() 接收两个参数：
//   1. 键名 ("xm-user")：用于后续从本地存储检索该数据时使用的标识符。
//   2. 值 (JSON.stringify(this.user))：将 this.user 对象通过 JSON.stringify() 方法序列化成JSON字符串，
//      因为本地存储只能处理字符串类型的数据，不能直接存储JavaScript对象。
// 当需要读取这个用户数据时，可以通过调用 localStorage.getItem("xm-user") 并使用 JSON.parse() 解析返回的字符串来还原成原始对象。*/
          this.fromVisible = false /*关掉充值对话框*/
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    update() {
      // 保存当前的用户信息到数据库
      this.$request.put('/user/update', this.user).then(res => {
        if (res.code === '200') {
          // 成功更新
          this.$message.success('保存成功')
          // 更新浏览器缓存里的用户信息
          localStorage.setItem('xm-user', JSON.stringify(this.user))

          // 触发父级的数据更新
          this.$emit('update:user')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAvatarSuccess(response, file, fileList) {
      // 把user的头像属性换成上传的图片的链接
      this.$set(this.user, 'avatar', response.data)
    },
    // 修改密码
    updatePassword() {
      this.dialogVisible = true
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request.put('/updatePassword', this.user).then(res => {
            if (res.code === '200') {
              // 成功更新
              this.$message.success('修改密码成功')
              this.$router.push('/login')
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
/deep/.el-form-item__label {
  font-weight: bold;
}
/deep/.el-upload {
  border-radius: 50%;
}
/deep/.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  cursor: url(https://ik.imagekit.io/nicexl/cursor/link.cur),default !important;
  position: relative;
  overflow: hidden;
  border-radius: 50%;
}
/deep/.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
  border-radius: 50%;
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
  border-radius: 50%;
}
</style>