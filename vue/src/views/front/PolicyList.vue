<template>
    <div class="main">
        <!--  头部  -->
        <div class="contain">
            <div class="article-list" style="padding-top: 30px;padding-right: 100px">
                <el-card v-for="(article, index) in paginatedArticles" :key="index" class="article-card">
                    <h3>{{ article.title }}</h3>
                    <div class="card-content">
                        <el-row>
                            <el-col :span="22">
                                <p>{{ article.content.substring(0, 100) }}{{
                                    article.content.length > 100 ? '...' : ''
                                    }}</p>
                            </el-col>
                            <el-col :span="2">
                                <span class="card-time">{{ article.date }}</span>
                            </el-col>
                        </el-row>
                    </div>
                </el-card>
                <!-- 分页组件 -->
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page="currentPage"
                        :page-sizes="[5, 10, 20]"
                        :page-size="pageSize"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="totalCount">
                </el-pagination>
            </div>
        </div>
    </div>
</template>

<script>

export default {
    name: "PolicyList",
    data() {
        return {
            articles: [{
                title: '文章标题1',
                content: '文章内容1',
                date: '2023-06-01'
            }, {
                title: '文章标题2',
                content: '文章内容2',
                date: '2023-06-02'
            },
                {
                    title: '文章标题2',
                    content: '文章内容2',
                    date: '2023-06-02'
                },
                {
                    title: '文章标题2',
                    content: '文章内容2',
                    date: '2023-06-02'
                },
            ], // 假设这是从API获取的文章列表
            currentPage: 1, // 当前页数
            pageSize: 10, // 每页显示的文章数量
            totalCount: 0, // 总文章数量
        };
    },
    computed: {
        // 计算属性用于获取当前页的分页文章列表
        paginatedArticles() {
            const start = (this.currentPage - 1) * this.pageSize;
            const end = start + this.pageSize;
            return this.articles.slice(start, end);
        },
    },
    methods: {
        handleSizeChange(val) {
            this.pageSize = val;
            this.fetchData(); // 假设这是重新获取数据的方法
        },
        handleCurrentChange(val) {
            this.currentPage = val;
            // 如果需要，这里也可以调用获取数据的方法，如果后端支持分页的话
        },
        // 假设的获取数据方法
        fetchData() {
            // 这里应该是调用API获取数据的逻辑，并更新articles和totalCount
        },
    },
    mounted() {
        // 页面挂载时获取数据
        this.fetchData();
    },
}
</script>

<style scoped>
.contain {
    flex: 1;
    height: 80vh;
    margin-left: 120px;
}

.article-card .card-content {
    display: flex;
    flex-direction: column;
}

.card-time {
    margin-top: 10px;
    color: #999;
}
</style>