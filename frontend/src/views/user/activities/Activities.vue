
<template>
  <div class="activities-container" style="width: 100%">
    <a-card :bordered="false" class="activities-card">
      <h2 class="page-title">活动列表</h2>

      <a-list
        :grid="{ gutter: 16, column: 1 }"
        :data-source="activitiesList"
        :loading="loading"
      >
        <a-list-item slot="renderItem" slot-scope="item">
          <a-card
            :class="['activity-item', { 'expired': isExpired(item.finishTime) }]"
            hoverable
          >
            <a-card-meta>
              <div slot="title" class="activity-title">
                {{ item.title }}
                <a-tag v-if="isExpired(item.finishTime)" color="red">已过期</a-tag>
                <a-tag v-else color="green">进行中</a-tag>
              </div>
              <div slot="description" class="activity-content">
                {{ item.content }}
              </div>
            </a-card-meta>

            <div class="activity-info">
              <div class="info-row">
                <a-icon type="team" />
                <span class="label">已报名人数：</span>
                <span :class="{ 'num-highlight': item.num > 0 }">{{ item.num || 0 }}人</span>
              </div>
              <div class="info-row">
                <a-icon type="user" />
                <span class="label">发布人：</span>
                <span>{{ item.publisher }}</span>
              </div>
              <div class="info-row">
                <a-icon type="clock-circle" />
                <span class="label">发布时间：</span>
                <span>{{ item.createdAt }}</span>
              </div>
              <div class="info-row">
                <a-icon type="schedule" />
                <span class="label">活动时间：</span>
                <span>{{ item.eventTime }}</span>
              </div>
              <div class="info-row">
                <a-icon type="check-circle" />
                <span class="label">截止时间：</span>
                <span>{{ item.finishTime }}</span>
              </div>
            </div>

            <div class="action-buttons">
              <a-button
                type="primary"
                :disabled="isExpired(item.finishTime)"
                @click="handleApply(item)"
              >
                {{ isExpired(item.finishTime) ? '已过期' : '立即报名' }}
              </a-button>
            </div>
          </a-card>
        </a-list-item>
      </a-list>
    </a-card>
  </div>
</template>

<script>
import {mapState} from 'vuex'
export default {
  name: "Activities",
  data() {
    return {
      activitiesList: [],
      loading: false
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
      this.$get('/cos/activities/list').then((r) => {
        this.activitiesList = r.data.data
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    isExpired(finishTime) {
      if (!finishTime) return false
      const finish = new Date(finishTime.replace(/-/g, '/'))
      const now = new Date()
      return finish < now
    },

    handleApply(item) {
      if (this.isExpired(item.finishTime)) {
        this.$message.warning('该活动已过期，无法报名')
        return
      }

      this.$confirm({
        title: '确认报名',
        content: `您确定要报名参加"${item.title}"吗？`,
        okText: '确认',
        cancelText: '取消',
        onOk: () => {
          this.$post('/cos/activity-participants', {
            activityId: item.id,
            userId: this.currentUser.userId
          }).then(() => {
            this.$message.success('报名成功！')
            this.queryActivitiesList()
          }).catch(() => {
            this.$message.error('报名失败，请重试')
          })
        }
      })
    },

    viewDetail(item) {
      this.$router.push({
        path: '/user/activities/detail',
        query: { id: item.id }
      })
    }
  }
}
</script>

<style scoped>.activities-container {
  padding: 24px;
}

.activities-card {
  background: #fff;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 24px;
  color: #333;
}

.activity-item {
  margin-bottom: 16px;
  transition: all 0.3s;
}

.activity-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.activity-item.expired {
  opacity: 0.7;
  background: #f5f5f5;
}

.activity-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 18px;
  font-weight: 600;
}

.activity-content {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin-top: 12px;
}

.activity-info {
  margin-top: 16px;
  padding: 16px 0;
  border-top: 1px solid #e8e8e8;
}

.info-row {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  font-size: 14px;
  color: #666;
}

.info-row .anticon {
  margin-right: 8px;
  color: #1890ff;
}

.info-row .label {
  font-weight: 500;
  width: 80px;
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
</style>
