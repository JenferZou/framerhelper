<template>
    <div class="main">
        <!--  头部  -->
        <div>
            <!--    顶部轮播图 ： Element->Carousel走马灯   -->
            <el-carousel :interval="4000" type="card" height="300px"> <!--interval="4000" : 4秒转换一次-->
                <el-carousel-item v-for="item in carouselData" :key="item">
                    <!--v-for="item in carouselDate" 轮播图片的数量由carouselDate方法中的图片数量决定-->
                    <img :src="item" alt="" style="width: 100%; height: 300px; border-radius: 5px">
                    <!-- border-radius：圆边-->
                </el-carousel-item>
            </el-carousel>
        </div>

        <div class="contain">
            <div class="article-list" style="padding-top: 30px;padding-right: 100px">
                <el-card v-for="(article, index) in articles" :key="index" class="article-card">
                    <h3>{{ article.name }}</h3>
                    <div class="card-content">
                        <el-row>
                            <el-col :span="22">
                                <p>{{ article.content.substring(0, 100) }}{{
                                    article.content.length > 100 ? '...' : ''
                                    }}</p>
                            </el-col>
                            <el-col :span="2">
                                <span class="card-time">{{ article.time }}</span>
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
    name: "InformationList",
    data() {
        return {
            carouselData: [
                /*静态图---然后动态替换*/
                require('@/assets/imgs/多彩贵州.jpg'),
                require('@/assets/imgs/绚烂湛江.jpg'),
                require('@/assets/imgs/腾冲和顺古镇.jpg'),
            ],
            articles: [
                {
                    name: '文章标题2',
                    content: '文章内容2',
                    date: '2023-06-02'
                },
            ], // 假设这是从API获取的文章列表
            currentPage: 1, // 当前页数
            pageSize: 10, // 每页显示的文章数量
            totalCount: 0, // 总文章数量
        };
    },
    // computed: {
    //     // 计算属性用于获取当前页的分页文章列表
    //     paginatedArticles() {
    //         const start = (this.currentPage - 1) * this.pageSize;
    //         const end = start + this.pageSize;
    //         return this.articles.slice(start, end);
    //     },
    // },
    methods: {
        handleSizeChange(val) {
            this.pageSize = val;
            this.fetchData(); // 假设这是重新获取数据的方法
        },
        handleCurrentChange(val) {
            this.currentPage = val;
            this.fetchData(); // 假设这是重新获取数据的方法
            console.log('当前页数：', val+'当前数据'+this.articles.value)
        },
        cleanHtmlTags(html) {
            // 使用更精确的正则表达式匹配HTML标签，并且去除 &nbsp;
            return html.replace(/<\/?[^>]+(>|$)|&nbsp;/g, '');
        },

        // 假设的获取数据方法
        fetchData() {
            this.$request.get('/agritainment/selectAllPage', {
                params: {
                    pageNum: this.currentPage,
                    pageSize: this.pageSize,
                },}
            ).then(res => {
                console.log(res)
                if (res.code === '200') {
                    this.articles = res.data.list.map(article => {
                        return {
                            ...article,
                            content: this.cleanHtmlTags(article.content) // 调用 cleanHtmlTags 函数
                        };
                    });
                    this.currentPage = res.data.pageNum
                    this.totalCount = res.data.total

                } else {
                    this.$message.error(res.msg)
                }
            })
        },
    },



    mounted() {
        // 页面挂载时获取数据
        this.fetchData();
    },
}
</script>

<style scoped>
.item-box {
    position: relative;
    width: 100%;
    height: 100%;
}
.carimg {
    width: 100%;
    height: 100%;
    overflow: hidden;
    object-fit: cover;
}
.desc-box {
    position: absolute;
    bottom: 0;
    left: 50%;
    top: 50%;
    width: 500px;
    height: 40px;
    margin-left: -250px;
    margin-top: -20px;
    text-align: center;
}

.main {
    background-color: #f2f4f8;
    min-height: 100vh;
    /*display: flex;*/
    justify-content: center;
    align-items: center;
}

.contain {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
}

.article-list {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.article-card {
    width: 90%;
    margin-bottom: 30px;
    border-radius: 12px;
    background-color: #fff;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease-in-out;
}

.article-card:hover {
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.article-title {
    font-size: 28px;
    margin-bottom: 10px;
    color: #333;
}

.article-content {
    font-size: 18px;
    margin-bottom: 10px;
    line-height: 1.6;
    color: #555;
}

.content-timestamp {
    color: #999;
}

.card-content {
    display: flex;
    flex-direction: column;
    padding: 20px;
}

.card-time {
    margin-top: auto;
    color: #999;
}
</style>
