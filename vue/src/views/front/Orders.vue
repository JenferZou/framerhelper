<template>
  <div class="main-content">
<!--    订单信息表格-->
    <div class="table" style="width: 80%; margin: 20px auto"> <!--居中 20px：上下外边距是20像素；auto：左右外边距自动计算，以使元素水平居中-->
      <div style="font-size: 17px; margin: 20px 10px">我的订单（{{ tableData.length }}）</div>  <!-- {{ tableData.length }}使用了Vue的插值语法来显示tableData数组的长度，即当前显示的订单数量-->
      <el-table :data="tableData" stripe> <!--:data="tableData" 使用Vue的绑定语法将tableData数组绑定到表格的数据源-->
<!--      使用 el-table-column 元素来定义表格的列，包括序号、商品图片、商品名称、价格、订单编号、下单用户、下单时间、订单状态和操作。  -->
<!--      Sortable — 是一个 JavaScript 库，用于在现代浏览器和触摸设备上对拖放列表进行重新排序。无需 jQuery。支持 Meteor、AngularJS、React、Polymer、Vue、Ember、Knockout 和任何 CSS 库，例如 Bootstrap。-->
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="img" label="商品图片" width="100">
          <template v-slot="scope">
            <div style="display: flex; align-items: center">
<!--             自定义模板，利用el-image组件来显示图片或无图片时的占位符。 -->
              <el-image style="width: 40px; height: 40px" v-if="scope.row.img"
                        :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="goodsName" label="商品名称" show-overflow-tooltip></el-table-column>
        <el-table-column prop="price" label="订单价格"></el-table-column>
        <el-table-column prop="orderId" label="订单编号"></el-table-column>
        <el-table-column prop="userName" label="下单用户"></el-table-column>
        <el-table-column prop="time" label="下单时间"></el-table-column>
        <el-table-column prop="status" label="订单状态" width="100"></el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
<!--           根据 scope.row.status 的值来决定显示哪个按钮。如果 scope.row.status 是 '已发货'，则显示一个“确认收货”的按钮；如果 scope.row.status 是 '已完成'，则显示一个“删除”的按钮。 -->
            <el-button plain type="primary" size="mini" v-if="scope.row.status === '已发货'" @click="changeStatus(scope.row, '已完成')">确认收货</el-button>
            <el-button plain type="danger" size="mini" v-if="scope.row.status === '已完成'" @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination" style="margin-top: 20px">
<!--        使用 el-pagination 元素来显示分页控件。-->
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
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 5,  // 每页显示的个数
      total: 0,
      orderId: null,
      status: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'), //localStorage中获取一个名为xm-user的项，并将其解析为一个JavaScript对象（user）。如果该项不存在或其值为一个“falsy”值，则返回一个空的JavaScript对象。
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    changeStatus(row, status) {
      let data = JSON.parse(JSON.stringify(row))
      data.status = status
      this.$request.put('/orders/update', data).then(res => {
        if (res.code === '200') {
          this.$message.success('操作成功')
          this.load(1)
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    del(id) {   // 单个删除   定义一个名为 del 的方法，该方法接受一个参数 id。
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {   // 使用 Vue 的 $confirm 方法弹出一个确认框，询问用户是否确定删除。如果用户点击“确定”，则执行回调函数 .then() 中的代码。
        this.$request.delete('/orders/delete/' + id).then(res => { //使用 $request 的 delete 方法发送一个 DELETE 请求到服务器的 /orders/delete/ 路径，后面加上传入的 id 参数。这实际上是删除指定 ID 的订单。
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('删除成功') //显示一个成功消息，告知用户订单已成功删除。
            this.load(1)  //调用 load 方法并传入参数 1。刷新页面
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => { //如果在发送 DELETE 请求时发生错误，则执行 .catch() 中的代码。当前，这个捕获块是空的，这意味着任何错误都不会在这里被处理。
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/orders/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          userId: this.user.id
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>

</style>