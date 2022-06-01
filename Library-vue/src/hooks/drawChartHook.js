// 用于绘制echarts图表的依赖
import * as echarts from 'echarts'
import {isRef, onBeforeUnmount, onMounted} from 'vue'
import elementResizeDetectorMaker from "element-resize-detector"

/**
 * 用于组件中绘制echarts的图表
 * @param chartDomRef 图表需要展示位置的dom元素的ref对象
 * @param getOptions 获取图表的方法，请传入一个函数，支持异步async函数
 */
export default function (chartDomRef,getOptions){
  // 判断参数是否满足要求
  if(isRef(chartDomRef) && Object.prototype.toString.call(getOptions) === '[object Function]'){

    // 图表对象
    let chart = null
    // 监听图表容器大小改变的检测器
    let chartDomResizeDetector = null

    // 当组件加载完毕才开始画图
    onMounted(async ()=>{
      if(chartDomRef.value){
        chart = echarts.init(chartDomRef.value)
        // 获取配置表
        const options = await getOptions()
        chart.setOption(options)

        // 使图表跟随容器的大小改变而改变
        chartDomResizeDetector = elementResizeDetectorMaker()
        chartDomResizeDetector.listenTo(chartDomRef.value,()=>{
          if(chart != null){
            chart.resize()
          }
        })
      }
    })
    // 当组件卸载前卸载相关对象，帮助垃圾回收
    onBeforeUnmount(()=>{
      // 这里注意销毁的顺序，不能改变，否则报错

      // 销毁监听对象大小改变的监听器
      chartDomResizeDetector && chartDomRef.value && chartDomResizeDetector.uninstall(chartDomRef.value)
      chartDomResizeDetector = null

      // 销毁图表
      chart && chart.dispose()
      chart = null
    })
  }
}
