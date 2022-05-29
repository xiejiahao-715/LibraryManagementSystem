<template>
  <div class="container">
    <Header class="header-wrapper"></Header>
    <div class="content-wrapper">
      <Sidebar class="sidebar-wrapper"></Sidebar>
      <Main class="main-wrapper"></Main>
    </div>
  </div>
</template>

<script>
import Sidebar from '@/layout/Sidebar'
import Header from "@/layout/Header";
import Main from "@/layout/Main";

export default {
  name: "Layout",
  components:{
    Main,
    Header,
    Sidebar
  }
}
</script>

<style scoped lang="scss">
.container{
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
  .header-wrapper{
    flex-shrink: 0;
  }
  .content-wrapper{
    display: flex;
    flex: 1;
    width: 100%;
    overflow: hidden;
    .sidebar-wrapper{
      flex-shrink: 0;
    }
    .main-wrapper{
      flex: 1;
      overflow: hidden;
    }
  }
}
</style>
