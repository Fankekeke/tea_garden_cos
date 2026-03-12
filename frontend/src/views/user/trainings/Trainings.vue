
<template>
  <div class="trainings-container">
    <a-card :bordered="false" class="trainings-card">
      <h2 class="page-title">培训课程</h2>

      <a-list
        :grid="{ gutter: 16, column: 1 }"
        :data-source="trainingsList"
        :loading="loading"
      >
        <a-list-item slot="renderItem" slot-scope="item">
          <a-card
            class="training-item"
            hoverable
          >
            <a-row :gutter="16">
              <a-col :span="8">
                <div class="video-thumbnail">
                  <a-icon type="play-circle" theme="filled" class="play-icon" />
                  <div class="video-overlay" @click="playVideo(item)">
                    <a-button type="primary" shape="round" icon="play-circle">
                      观看视频
                    </a-button>
                  </div>
                </div>
              </a-col>
              <a-col :span="16">
                <a-card-meta>
                  <div slot="title" class="training-title">
                    {{ item.title }}
                  </div>
                  <div slot="description">
                    <div class="training-description">
                      {{ item.description }}
                    </div>
                    <div class="training-footer">
                      <div class="info-row">
                        <a-icon type="clock-circle" />
                        <span class="label">发布时间：</span>
                        <span>{{ item.createdAt }}</span>
                      </div>
                    </div>
                  </div>
                </a-card-meta>
              </a-col>
            </a-row>

            <div class="action-buttons">
              <a-button type="primary" icon="play-circle" @click="playVideo(item)">
                立即学习
              </a-button>
            </div>
          </a-card>
        </a-list-item>
      </a-list>
    </a-card>

    <!-- 视频播放弹窗 -->
    <a-modal
      v-model="videoVisible"
      :title="currentTraining.title"
      :width="900"
      :footer="null"
      @cancel="closeVideo"
      :bodyStyle="{ padding: 0 }"
    >
      <div class="video-player" v-if="currentTraining.videoUrl">
        <video
          :key="videoKey"
          :src="'http://127.0.0.1:9527/imagesWeb/' + currentTraining.videoUrl"
          controls
          autoplay          style="width: 100%; max-height: 500px;"
        >
          您的浏览器不支持视频播放
        </video>
      </div>
    </a-modal>
  </div>
</template>



<script>import {mapState} from 'vuex'
export default {
  name: "Trainings",
  data() {
    return {
      trainingsList: [],
      loading: false,
      videoVisible: false,
      currentTraining: {},
      videoKey: 0
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
      this.$get('/cos/trainings/list').then((r) => {
        console.log(r)
        if (r.data && r.data.data) {
          this.trainingsList = r.data.data
        } else if (Array.isArray(r.data)) {
          this.trainingsList = r.data
        } else {
          this.trainingsList = []
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    playVideo(item) {
      this.currentTraining = item
      this.videoVisible = true
      this.videoKey = Date.now()
    },

    closeVideo() {
      this.videoVisible = false
      this.currentTraining = {}
      this.videoKey = 0
    }
  }
}
</script>



<style scoped>.trainings-container {
  padding: 24px;
}

.trainings-card {
  background: #fff;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 24px;
  color: #333;
}

.training-item {
  margin-bottom: 16px;
  transition: all 0.3s;
}

.training-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.video-thumbnail {
  position: relative;
  width: 100%;
  height: 200px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  cursor: pointer;
}

.play-icon {
  font-size: 64px;
  color: rgba(255, 255, 255, 0.8);
  opacity: 0.6;
}

.video-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.video-thumbnail:hover .video-overlay {
  opacity: 1;
}

.video-player {
  width: 100%;
  background: #000;
  border-radius: 4px;
}

.training-title {
  font-size: 18px;
  font-weight: 600;
  color: #1890ff;
  margin-bottom: 12px;
}

.training-description {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 12px;
  background: #f9f9f9;
  padding: 12px;
  border-radius: 4px;
}

.training-footer {
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
  min-width: 120px;
}

::v-deep .ant-modal-body {
  padding: 0;
}

::v-deep .ant-modal-header {
  margin: 0;
  padding: 16px 24px;
  border-bottom: 1px solid #e8e8e8;
}

::v-deep .ant-modal-close {
  top: 16px;
}
</style>
