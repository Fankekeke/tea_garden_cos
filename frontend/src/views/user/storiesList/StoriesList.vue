
<template>
  <div class="stories-container">
    <a-card :bordered="false" class="stories-card">
      <h2 class="page-title">茶农故事</h2>

      <a-list
        :grid="{ gutter: 16, column: 1 }"
        :data-source="storiesList"
        :loading="loading"
      >
        <a-list-item slot="renderItem" slot-scope="item">
          <a-card
            class="story-item"
            hoverable
            @click="viewDetail(item)"
          >
            <a-row :gutter="16">
              <a-col :span="4">
                <div class="author-avatar">
                  <a-avatar
                    v-if="item.images"
                    :src="'http://127.0.0.1:9527/imagesWeb/' + item.images"
                    :size="64"
                  />
                  <a-avatar v-else icon="user" :size="64" />
                </div>
              </a-col>
              <a-col :span="20">
                <a-card-meta>
                  <div slot="title" class="story-title">
                    {{ item.title }}
                    <a-tag color="green" style="margin-left: 10px;">已审核</a-tag>
                  </div>
                  <div slot="description">
                    <div class="author-info">
                      <a-icon type="user" />
                      <span class="label">作者：</span>
                      <span>{{ item.name }}</span>
                      <a-divider type="vertical" />
                      <a-icon type="idcard" />
                      <span class="label">编号：</span>
                      <span>{{ item.code }}</span>
                      <a-divider type="vertical" />
                      <a-icon type="gender" />
                      <span class="label">性别：</span>
                      <a-tag :color="item.sex === 1 ? 'blue' : 'pink'">
                        {{ item.sex === 1 ? '男' : '女' }}
                      </a-tag>
                    </div>

                    <div class="story-content">
                      {{ item.content }}
                    </div>

                    <div class="story-footer">
                      <div class="info-row">
                        <a-icon type="clock-circle" />
                        <span class="label">发布时间：</span>
                        <span>{{ item.createdAt }}</span>
                      </div>
                      <div class="info-row" v-if="item.auditRemark">
                        <a-icon type="check-circle" />
                        <span class="label">审核备注：</span>
                        <span>{{ item.auditRemark }}</span>
                      </div>
                    </div>
                  </div>
                </a-card-meta>
              </a-col>
            </a-row>

            <div class="action-buttons">
              <a-button type="primary" @click.stop="viewDetail(item)">
                阅读全文
              </a-button>
<!--              <a-button @click.stop="shareStory(item)">-->
<!--                分享故事-->
<!--              </a-button>-->
            </div>
          </a-card>
        </a-list-item>
      </a-list>
    </a-card>

    <!-- 详情抽屉 -->
    <a-drawer
      title="故事详情"
      :visible="detailVisible"
      :width="800"
      @close="handleDetailClose"
      placement="right"
    >
      <div v-if="currentStory" class="story-detail">
        <div class="detail-header">
          <a-row :gutter="16">
            <a-col :span="3">
              <a-avatar
                v-if="currentStory.images"
                :src="'/static/upload/' + currentStory.images"
                :size="80"
              />
              <a-avatar v-else icon="user" :size="80" />
            </a-col>
            <a-col :span="21">
              <h3 class="detail-title">{{ currentStory.title }}</h3>
              <div class="detail-author">
                <a-tag color="green">已审核</a-tag>
                <span class="author-name">{{ currentStory.name }}</span>
                <span class="author-code">{{ currentStory.code }}</span>
              </div>
            </a-col>
          </a-row>
        </div>

        <a-divider />

        <div class="detail-content">
          <p>{{ currentStory.content }}</p>
        </div>

        <a-divider />

        <div class="detail-footer">
          <a-descriptions bordered :column="2">
            <a-descriptions-item label="作者姓名">
              {{ currentStory.name }}
            </a-descriptions-item>
            <a-descriptions-item label="作者编号">
              {{ currentStory.code }}
            </a-descriptions-item>
            <a-descriptions-item label="性别">
              <a-tag :color="currentStory.sex === 1 ? 'blue' : 'pink'">
                {{ currentStory.sex === 1 ? '男' : '女' }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="发布时间">
              {{ currentStory.createdAt }}
            </a-descriptions-item>
            <a-descriptions-item label="审核状态" :span="2">
              <a-tag color="green">通过</a-tag>
            </a-descriptions-item>
            <a-descriptions-item
              v-if="currentStory.auditRemark"
              label="审核备注"
              :span="2"
            >
              {{ currentStory.auditRemark }}
            </a-descriptions-item>
          </a-descriptions>
        </div>
      </div>
    </a-drawer>
  </div>
</template>


<script>import {mapState} from 'vuex'
export default {
  name: "StoriesList",
  data() {
    return {
      storiesList: [],
      loading: false,
      detailVisible: false,
      currentStory: null
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  mounted() {
    this.queryActivitiesList()
  },
  methods: {
    queryActivitiesList() {
      this.loading = true
      this.$get('/cos/stories/queryListApproved').then((r) => {
        console.log(r)
        if (r.data && r.data.data) {
          this.storiesList = r.data.data
        } else if (Array.isArray(r.data)) {
          this.storiesList = r.data
        } else {
          this.storiesList = []
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    viewDetail(item) {
      this.currentStory = item
      this.detailVisible = true
    },

    handleDetailClose() {
      this.detailVisible = false
      this.currentStory = null
    },

    shareStory(item) {
      const url = window.location.origin + '/#/user/stories/detail?id=' + item.id
      this.$copyText(url).then(() => {
        this.$message.success('链接已复制到剪贴板，可以分享给好友了！')
      }).catch(() => {
        this.$message.info('分享链接：' + url)
      })
    }
  }
}
</script>


<style scoped>.stories-container {
  padding: 24px;
}

.stories-card {
  background: #fff;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 24px;
  color: #333;
}

.story-item {
  margin-bottom: 16px;
  transition: all 0.3s;
  cursor: pointer;
}

.story-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.author-avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.story-title {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
  color: #1890ff;
}

.author-info {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 16px;
  font-size: 14px;
  color: #666;
}

.author-info .anticon {
  color: #1890ff;
}

.author-info .label {
  font-weight: 500;
}

.story-content {
  font-size: 14px;
  color: #333;
  line-height: 1.8;
  margin-bottom: 16px;
  max-height: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 6;
  -webkit-box-orient: vertical;
  background: #f9f9f9;
  padding: 12px;
  border-radius: 4px;
}

.story-footer {
  padding-top: 12px;
  border-top: 1px solid #e8e8e8;
}

.info-row {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  font-size: 13px;
  color: #999;
}

.info-row .anticon {
  margin-right: 6px;
  color: #1890ff;
}

.info-row .label {
  font-weight: 500;
  width: 70px;
}

.action-buttons {
  display: flex;
  gap: 12px;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #e8e8e8;
}

.action-buttons .ant-btn {
  min-width: 100px;
}

.story-detail {
  padding-bottom: 40px;
}

.detail-header {
  margin-bottom: 24px;
}

.detail-title {
  font-size: 24px;
  font-weight: 600;
  color: #1890ff;
  margin-bottom: 12px;
}

.detail-author {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 8px;
}

.author-name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.author-code {
  font-size: 14px;
  color: #999;
}

.detail-content {
  font-size: 15px;
  line-height: 2;
  color: #333;
  white-space: pre-wrap;
  word-break: break-all;
  padding: 20px 0;
}

.detail-footer {
  margin-top: 24px;
}
</style>
