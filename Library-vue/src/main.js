import { createApp } from 'vue'
import App from '@/App.vue'
import router from '@/router'
import '@/router/permission'

import {createPinia} from 'pinia'

// 导入全局样式
import '@/assets/style/global.scss'

// 导入ELMessage组件的样式
import 'element-plus/es/components/message/style/css'
// 导入加载组件loading的样式
import 'element-plus/es/components/loading/style/css'

createApp(App)
    .use(router)
    .use(createPinia())
    .mount('#app')
