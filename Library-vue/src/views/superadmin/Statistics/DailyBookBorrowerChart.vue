<template>
  <div ref="dailyBookBorrowChartRef"></div>
</template>

<script>
import {shallowRef} from "vue";
import drawChartHook from "@/hooks/drawChartHook";
import {formatDate} from '@/utils'
import {getDailyBookBorrowNumApi} from "@/api/superadmin";

export default {
  // 折线图组件
  name: "DailyBookBorrowerChart",
  setup() {
    const dailyBookBorrowChartRef = shallowRef()

    // 绘制图表
    drawChartHook(dailyBookBorrowChartRef, getOptions)

    // 获取表格需要填充的数据
    async function getChartData(){
      // 获取明天的时间
      let tomorrow = new Date()
      tomorrow.setTime(tomorrow.getTime() + 24 * 60 * 60 * 1000)
      // 向前偏移30天
      const {data} = await getDailyBookBorrowNumApi(formatDate(tomorrow,'yyyy-MM-dd'),-30)
      if(data && Array.isArray(data.list)){
        // 设置x轴数据和y轴数据
        const chartData = {
          xAxisData: [],
          seriesData: []
        }
        for (let item of data.list){
          chartData.xAxisData.push(item.date)
          chartData.seriesData.push(item.num)
        }
        return chartData
      }else {
        return null
      }
    }
    // 获取表格的配置项
    async function getOptions() {
      const chartData = await getChartData()
      if(chartData){
        return {
          title:{
            text: '图书近30天的日借阅量',
            left: 'center',
            textStyle:{
              fontSize: '15'
            }
          },
          tooltip: {
            trigger: 'axis'
          },
          xAxis: {
            name: '日期',
            type: 'category',
            data: chartData.xAxisData
          },
          yAxis: {
            name: '日借阅量',
            type: 'value',
          },
          series: [
            {
              name: '日借阅量',
              data: chartData.seriesData,
              type: 'line',
              smooth: true
            }
          ]
        }
      }else {
        return null
      }
    }

    return {
      dailyBookBorrowChartRef
    }
  }
}
</script>
