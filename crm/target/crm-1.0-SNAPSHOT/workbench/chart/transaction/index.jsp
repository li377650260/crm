<%--
  Created by IntelliJ IDEA.
  User: li377650260
  Date: 2021/6/19
  Time: 21:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>
    <script type="text/javascript" src="ECharts/echarts.min.js"></script>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
</head>
<script>
    $(function () {
        // 页面加载完毕后，绘制统计图标
        getCharts();
    });

    function getCharts() {

        $.ajax({
            url:"workbench/transaction/getCharts.do",
            type:"get",
            dataType:"json",
            success:function (data) {
                /*
                data={[total:100],[[value: 60, name: '访问'],[value: 60, name: '访问']]}
                 */
                // 基于dom容器，初始化echarts实例
                var myCharts = echarts.init(document.getElementById('main'));

                // 指定图表的配置项和数据
                option = {
                    title: {
                        text: '交易漏斗图',
                        subtext: '统计交易阶段数量'
                    },
                    // tooltip: {
                    //     trigger: 'item',
                    //     formatter: "{a} <br/>{b} : {c}%"
                    // },
                    // toolbox: {
                    //     feature: {
                    //         dataView: {readOnly: false},
                    //         restore: {},
                    //         saveAsImage: {}
                    //     }
                    // },
                    // legend: {
                    //     data: ['展现','点击','访问','咨询','订单']
                    // },

                    series: [
                        {
                            name:'交易漏斗图',
                            type:'funnel',
                            left: '10%',
                            top: 60,
                            //x2: 80,
                            bottom: 60,
                            width: '80%',
                            // height: {totalHeight} - y - y2,
                            min: 0,
                            max: data.total,
                            minSize: '0%',
                            maxSize: '100%',
                            sort: 'descending',
                            gap: 2,
                            label: {
                                show: true,
                                position: 'inside'
                            },
                            labelLine: {
                                length: 10,
                                lineStyle: {
                                    width: 1,
                                    type: 'solid'
                                }
                            },
                            itemStyle: {
                                borderColor: '#fff',
                                borderWidth: 1
                            },
                            emphasis: {
                                label: {
                                    fontSize: 20
                                }
                            },
                            data: data.dataList
                            // [
                            //     {value: 60, name: '访问'},
                            //     {value: 40, name: '咨询'},
                            //     {value: 20, name: '订单'},
                            //     {value: 80, name: '点击'},
                            //     {value: 100, name: '展现'}
                            // ]
                        }
                    ]
                };

                // 使用刚指定的配置项和数据显示图表。
                myCharts.setOption(option);
            },
        });


    }
</script>
<body>
<%--
       为Echarts统计图标准备一个具备高宽的DOM容器（Div）

--%>

<div id="main" style="width: 800px;height: 600px"></div>
</body>
</html>
