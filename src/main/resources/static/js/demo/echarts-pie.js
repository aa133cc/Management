$(function () {
    var pieChart = echarts.init(document.getElementById("echarts-pie-chart"));
    $.get('/product/pie',function (data) {
        var pieoption = {
            title : {
                text: '商品销量统计',
                subtext: '纯属虚构',
                x:'center'
            },
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                orient : 'vertical',
                x : 'left',
                data:data.title
            },
            calculable : true,
            series : [
                {
                    name:'访问来源',
                    type:'pie',
                    radius : '55%',
                    center: ['50%', '60%'],
                    data:data.data
                }
            ]
        };
        pieChart.setOption(pieoption);
        $(window).resize(pieChart.resize);
    });
});
