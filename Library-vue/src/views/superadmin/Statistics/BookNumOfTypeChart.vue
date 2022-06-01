<template>
  <div ref="bookNumOfTypeChartRef"></div>
</template>

<script>
import {shallowRef} from "vue";
import drawChartHook from "@/hooks/drawChartHook";
import {getBookNumOfTypeApi} from "@/api/superadmin";

export default {
  name: "BookNumOfTypeChart",
  setup(){
    const bookNumOfTypeChartRef = shallowRef()

    drawChartHook(bookNumOfTypeChartRef,getOptions)

    // 获取图标所需要的数据
    async function getChartData(){
      const {data} = await getBookNumOfTypeApi()
      if(data && Array.isArray(data.list)){
        const chartData = {
          seriesData: []
        }
        for(let item of data.list){
          chartData.seriesData.push({
            value: item.num,
            name: item.typeName
          })
        }
        return chartData
      }else {
        return null
      }
    }
    async function getOptions(){
      const chartData = await getChartData()
      if(chartData){
        return {
          title: {
            text: '图书馆藏信息',
            left:'center'
          },
          tooltip: {
            trigger: 'item'
          },
          legend: {
            orient: 'vertical',
            left: 'left'
          },
          series: [
            {
              type: 'pie',
              radius: '50%',
              data: chartData.seriesData,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              },
              label:{
                show: true,
                formatter: '{d}%' //只要百分比
              },
              labelLine :{show:true}
            }
          ]
        };
      }else {
        return null
      }
    }

    return {
      bookNumOfTypeChartRef
    }
  }
}
</script>
