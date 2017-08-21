$(function () {
    $(".chk").click(getAllids);


    document.getElementById("chk_open").onclick = function() {
        document.getElementById("opreationHidden").value = "open";

        document.getElementById("opreation_submit").click();
    };
    document.getElementById("chk_auto").onclick = function() {
        document.getElementById("opreationHidden").value = "auto";

        document.getElementById("opreation_submit").click();
    };
    document.getElementById("chk_close").onclick = function() {
        document.getElementById("opreationHidden").value = "close";

        document.getElementById("opreation_submit").click();
    };
})
function getAllids() {
    var checkbox = document.getElementsByClassName("chk");
    console.log(checkbox.length);

    var id = "";
    for(var i = 0; i < checkbox.length; i++){
        if (checkbox[i].checked){
            id = id + checkbox[i].value+"-";
        }
    }

    document.getElementById("sl_ids").value = id;

    console.log(document.getElementById("sl_ids").value);

    return id;
}


window.onload=function()
{
    var arr =document.getElementsByTagName('input');
    var arr = document.getElementsByClassName("chk");
    var b = document.getElementById("check*");
    var c = document.getElementById("checkf");
    var iSelect=true;

    b.onclick = function() {

        if(iSelect) {
            for (var i = 0; i < arr.length; i++) {
                arr[i].checked = true;
            }
            iSelect = false;
            b.value = '取消';
        } else if(iSelect==false) {
            for(var i=0;i<arr.length;i++)
            {
                arr[i].checked=false;
            }
            iSelect=true;
            b.value='全选';
        }
        getAllids();
    }
    c.onclick = function () {
        for (var i = 0; i < arr.length; i++) {
            if (arr[i].checked == true) {
                arr[i].checked = false;
            }
            else {
                arr[i].checked = true;
            }
        }
        getAllids();
    }
}

function filter() {
    var oTab=document.getElementById("tab");
    var oBt=document.getElementsByName("ck");

    // 处理出正则表达式
    var str = '.*';
    for (var i = 0; i <= oBt[0].value.length - 1; i++) {
        str += oBt[0].value[i];
        str += '.*';
    }

    var patt1 = new RegExp(str, 'g');

    for(var i=0; i<oTab.tBodies[0].rows.length; i++)
    {
        var str2=oTab.tBodies[0].rows[i].cells[1].innerHTML.toUpperCase();
        var str3=oTab.tBodies[0].rows[i].cells[2].innerHTML.toUpperCase();
        var str4=oTab.tBodies[0].rows[i].cells[3].innerHTML.toUpperCase();

        var strfive = patt1.exec(str2) + "," + patt1.exec(str2) + "," + patt1.exec(str4);

        if (patt1.exec(str2) != null || patt1.exec(str3) != null || patt1.exec(str4) != null) {
            oTab.tBodies[0].rows[i].style.display='table-row';
        } else {
            oTab.tBodies[0].rows[i].style.display='none';
        }
    }
}

function changeOpreation(opreation, phone) {
    document.getElementById("oprea" + phone).value = opreation;
}

function ft() {
    var oTab1=document.getElementById("table_gl");
    var oBt1=document.getElementById("gl_cx")

    // 处理出正则表达式
    var str1 = '.*';
    for (var i = 0; i <= oBt1.value.length - 1; i++) {
        str1 += oBt1.value[i];
        str1 += '.*';
    }
    var patt1 = new RegExp(str1, 'g');

    for(var i=0; i<oTab1.tBodies[0].rows.length; i++)
    {
        var str2=oTab1.tBodies[0].rows[i].cells[0].innerHTML.toUpperCase();
        var str3=oTab1.tBodies[0].rows[i].cells[1].innerHTML.toUpperCase();
        var str4=oTab1.tBodies[0].rows[i].cells[2].innerHTML.toUpperCase();
        var str5=oTab1.tBodies[0].rows[i].cells[3].innerHTML.toUpperCase();
        var str6=oTab1.tBodies[0].rows[i].cells[4].innerHTML.toUpperCase();

        var strfive = patt1.exec(str2) + "," + patt1.exec(str2) + "," + patt1.exec(str4)+","+patt1.exec(str5)+","+patt1.exec(str6);

        if (patt1.exec(str2) != null || patt1.exec(str3) != null || patt1.exec(str4) != null||patt1.exec(str5) != null || patt1.exec(str6) != null) {
            oTab1.tBodies[0].rows[i].style.display='table-row';
        } else {
            oTab1.tBodies[0].rows[i].style.display='none';
        }
    }
}