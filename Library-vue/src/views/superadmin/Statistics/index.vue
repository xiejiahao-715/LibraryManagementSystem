<template>
  <div class="statistics">
    <el-card style="width: 100%;height: 300px;margin-bottom: 20px;min-width: 500px">
      <DailyBookBorrowerChart style="height: 100%"></DailyBookBorrowerChart>
    </el-card>
    <el-card style="width: 45%;height: 300px;min-width: 400px">
      <BookNumOfTypeChart style="height: 100%"></BookNumOfTypeChart>
    </el-card>
  </div>
</template>

<script>
import DailyBookBorrowerChart from "@/views/superadmin/Statistics/DailyBookBorrowerChart";
import BookNumOfTypeChart from "@/views/superadmin/Statistics/BookNumOfTypeChart";

export default {
  name: "Statistics",
  components:{
    DailyBookBorrowerChart,
    BookNumOfTypeChart
  },
  setup(){

  }
}
</script>

<style scoped lang="scss">
.statistics::v-deep{
  padding: 20px 30px 30px 20px;
  width: 100%;
  box-sizing: border-box;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  .el-card{
    .el-card__body{
      height: 100%;
      box-sizing: border-box;
      padding-bottom: 0;
    }
  }
}
</style>
