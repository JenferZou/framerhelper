<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入标题查询" style="width: 200px" v-model="name"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation">
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe  @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
<!--        Sortable — 是一个 JavaScript 库，用于在现代浏览器和触摸设备上对拖放列表进行重新排序。无需 jQuery。支持 Meteor、AngularJS、React、Polymer、Vue、Ember、Knockout 和任何 CSS 库，例如 Bootstrap。-->
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="name" label="资讯标题" show-overflow-tooltip></el-table-column>
        <el-table-column prop="content" label="资讯内容"> <!--show-overflow-tooltip-->
          <template v-slot="scope">
            <el-button type="primary" size="mini" @click="viewData(scope.row.content)">点击查看</el-button>  <!--点击后拿到scope这一行row的内容content-->
          </template>
        </el-table-column>
        <el-table-column prop="time" label="发布时间"></el-table-column>
<!--        <el-table-column prop="user" label="发布时间"></el-table-column>-->
        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini">编辑</el-button>
            <el-button plain type="danger" size="mini" @click=del(scope.row.id)>删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>

    <!--对话框 弹窗-->
    <el-dialog title="信息" :visible.sync="fromVisible" width="60%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="name" label="资讯标题">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="content" label="资讯内容">
<!--        文本框-->
<!--        <el-input type="textarea" :rows="5" v-model="form.content" autocomplete="off"></el-input>-->
<!--        富文本  先引入，再初始化-->
          <div id="editor"></div>  <!--给id是为了能让他找到地方，知道在这里富文本初始化-->
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="扶贫资讯" :visible.sync="editorVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
      <div v-html="this.viewContent" class="w-e-text"></div>  <!-- v-html 带有标签的展示里面的内容-->
    </el-dialog>

  </div>
</template>

<script>
/*引入富文本*/
import E from 'wangeditor'

export default {
  name: "Information",
  data() {
    return {
      editor: null,  //富文本初始化
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      name: null,
      fromVisible: false,
      editorVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        name: [
          {required: true, message: '请输入标题', trigger: 'blur'},
        ],
        // content: [
        //   {required: true, message: '请输入内容', trigger: 'blur'},
        // ]
      },
      ids: [],
      viewContent: null
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    initWangEditor(content) {
      this.$nextTick(() => {
        this.editor = new E('#editor')
        this.editor.config.placeholder = '请输入内容' /*placeholder ： 没输入时默认输出一句话*/
        this.editor.config.uploadFileName = 'file' /*uploadFileName ： 后台文件富文本接收的时候 用file名*/
        this.editor.config.uploadImgServer = 'http://localhost:9090/files/wang/upload' /*后台服务端口号同yml文件，uploadImgServer点击上传图片后，就会调用这个接口*/
        this.editor.create()
        //定时器函数，参数为以毫秒为单位的时间和一个指定函数，当指定时间过后调用该函数
        setTimeout(() => {
          this.editor.txt.html(content) /*赋值富文本初始化的内容*/
        })
      })
    },
    viewData(content) {
      this.viewContent = content //把content赋值给viewContent
      this.editorVisible = true //把对话框弹开
    },
    handleAdd() {   // 新增数据
      this.form = {}  // 新增数据的时候清空数据
      this.fromVisible = true  // 打开弹窗
      this.initWangEditor('')  //调用初始化的富文本编辑器
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
      this.initWangEditor(this.form.content || '')  //调用初始化的富文本编辑器 ,把this.form.content内容放进去,如果没有内容就给他一个空
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
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
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.content = this.editor.txt.html()  //点击保存后我们手动拿到富文本的内容content，因为它不是插件，不能自动渲染
          this.$request({
            url: this.form.id ? '/information/update' : '/information/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/information/delete/' + id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    handleSelectionChange(rows) {   // 当前选中的所有的行数据
      this.ids = rows.map(v => v.id)   //  [1,2]
    },
    delBatch() {   // 批量删除
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/information/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/information/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.name = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>

</style>
