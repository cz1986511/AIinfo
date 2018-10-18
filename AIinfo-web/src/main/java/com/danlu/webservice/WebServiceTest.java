package com.danlu.webservice;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class WebServiceTest
{
    public static void main(String[] args)
    {
        try
        {
            String wsdlUrl = "http://localhost:8080/fcmq/webservice/instinctCheck"; // 接口地址
            String nameSpaceUri = "http://webservice.web.mq.fc.com/"; // 命名空间地址（上传接口方法所在命名空间）
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(wsdlUrl));
            call.setOperationName(new QName(nameSpaceUri, "getInsCheckResult"));

            call.addParameter("inputXMLString", org.apache.axis.Constants.XSD_STRING,
                ParameterMode.IN);
            call.setReturnType(org.apache.axis.Constants.XSD_STRING);

            call.setReturnType(org.apache.axis.Constants.XSD_STRING);
            String postdata = "<ApplicationSchema><Application><Organisation>FLT</Organisation>"
                              + "<Country_Code>CN</Country_Code><Group_Member_Code></Group_Member_Code><Application_Number>1-6EYB6Z</Application_Number>"
                              + "<Application_Date>14/01/2018</Application_Date><Application_Type>LOAN</Application_Type><Amount_Limit></Amount_Limit>"
                              + "<Branch>成都直销部</Branch><Decision></Decision><Decision_Reason></Decision_Reason><Decision_Date></Decision_Date>"
                              + "<User_Field1></User_Field1><User_Field2>课栈教育分期合作方案</User_Field2><User_Field3></User_Field3><User_Field4></User_Field4>"
                              + "<User_Field5>1-65MA0X</User_Field5><User_Field6>北京弟傲思时代信息技术有限公司</User_Field6><User_Field7></User_Field7>"
                              + "<User_Field8>VRO0005</User_Field8><User_Field9>上海恒企教育培训有限公司</User_Field9>"
                              + "<User_Field10>中国工商银行股份有限公司广州永平支行</User_Field10><User_Field11>HQ01005</User_Field11><User_Field12></User_Field12>"
                              + "<User_Field13>UnSecure</User_Field13><User_Field14>PBOC前</User_Field14><User_Field15>无忧通关班（本科）</User_Field15>"
                              + "<User_Field16>财会</User_Field16><User_Field17>上海恒企教育培训有限公司</User_Field17><User_Field18></User_Field18>"
                              + "<User_Field19>待业</User_Field19><User_Field20>0.8889</User_Field20><User_Field21>0</User_Field21><User_Field22></User_Field22>"
                              + "<User_Field23></User_Field23><User_Field24>否</User_Field24><User_Field26>Y</User_Field26><User_Field27></User_Field27>"
                              + "<User_Field28>18</User_Field28><User_Field29>13760</User_Field29><User_Field30>0</User_Field30>"
                              + "<Applicant><ID_NUMBER1>510403197710250315</ID_NUMBER1><Id_Number2>1-6EYB5M</Id_Number2><Id_Number3>NoMatchRowId</Id_Number3>"
                              + "<Surname>石家敏</Surname><First_Name></First_Name><Sex>M</Sex><Date_Of_Birth>25/10/1977</Date_Of_Birth>"
                              + "<Home_Address1>四川省攀枝花西　区建兴路22号12栋1单元1号</Home_Address1><Home_Address6>其他</Home_Address6>"
                              + "<Home_Phone_Number>-</Home_Phone_Number><Mobile_Phone_Number>18784370370</Mobile_Phone_Number><Company_Name>无</Company_Name>"
                              + "<Company_Address1>无无无无</Company_Address1><Company_Address6>0</Company_Address6><Company_Phone_Number></Company_Phone_Number>"
                              + "<User_Field1>未婚</User_Field1><User_Field2>高中</User_Field2><User_Field3></User_Field3><User_Field4></User_Field4>"
                              + "<User_Field5></User_Field5><User_Field6></User_Field6><User_Field7></User_Field7><User_Field8></User_Field8>"
                              + "<User_Field9>线上合作方推荐</User_Field9><User_Field10></User_Field10><User_Field13></User_Field13><User_Field14></User_Field14>"
                              + "<User_Field15></User_Field15><User_Field16></User_Field16><User_Field17></User_Field17><User_Field18></User_Field18>"
                              + "<User_Field19>N</User_Field19><User_Field20>否</User_Field20></Applicant><A_Accountant_Solicitor><Surname></Surname>"
                              + "<Company_Name></Company_Name><Company_Address1></Company_Address1><Company_Address2>主要申请人</Company_Address2>"
                              + "<Company_Address6></Company_Address6><User_Field1>公积金</User_Field1><User_Field2></User_Field2><User_Field3></User_Field3>"
                              + "<User_Field4></User_Field4><User_Field5></User_Field5><User_Field6></User_Field6><User_Field7></User_Field7>"
                              + "<User_Field8></User_Field8><User_Field9></User_Field9><User_Field10></User_Field10></A_Accountant_Solicitor>"
                              + "<A_Accountant_Solicitor><Surname></Surname><Company_Name></Company_Name><Company_Address1></Company_Address1>"
                              + "<Company_Address2>主要申请人</Company_Address2><Company_Address6></Company_Address6><User_Field1>养老保险</User_Field1>"
                              + "<User_Field2></User_Field2><User_Field3></User_Field3><User_Field4></User_Field4><User_Field5></User_Field5>"
                              + "<User_Field6></User_Field6><User_Field7></User_Field7><User_Field8></User_Field8><User_Field9></User_Field9>"
                              + "<User_Field10></User_Field10></A_Accountant_Solicitor><A_Accountant_Solicitor><Surname></Surname><Company_Name></Company_Name>"
                              + "<Company_Address1></Company_Address1><Company_Address2>主要申请人</Company_Address2><Company_Address6></Company_Address6>"
                              + "<User_Field1>公积金</User_Field1><User_Field2></User_Field2><User_Field3></User_Field3><User_Field4></User_Field4>"
                              + "<User_Field5></User_Field5><User_Field6></User_Field6><User_Field7></User_Field7><User_Field8></User_Field8>"
                              + "<User_Field9></User_Field9><User_Field10></User_Field10></A_Accountant_Solicitor><A_Accountant_Solicitor><Surname></Surname>"
                              + "<Company_Name></Company_Name><Company_Address1></Company_Address1><Company_Address2>主要申请人</Company_Address2>"
                              + "<Company_Address6></Company_Address6><User_Field1>养老保险</User_Field1><User_Field2></User_Field2><User_Field3></User_Field3>"
                              + "<User_Field4></User_Field4><User_Field5></User_Field5><User_Field6></User_Field6><User_Field7></User_Field7>"
                              + "<User_Field8></User_Field8><User_Field9></User_Field9><User_Field10></User_Field10></A_Accountant_Solicitor>"
                              + "<Guarantor><Id_Number1></Id_Number1><Id_Number3></Id_Number3><Surname>吴群</Surname><Sex></Sex><Date_Of_Birth></Date_Of_Birth>"
                              + "<Home_Address2></Home_Address2><Home_Address3></Home_Address3><Home_Phone_Number></Home_Phone_Number>"
                              + "<Mobile_Phone_Number>18081791082</Mobile_Phone_Number><User_Field1>紧急联系人</User_Field1><User_Field3></User_Field3>"
                              + "<User_Field6></User_Field6></Guarantor><Guarantor><Id_Number1></Id_Number1><Id_Number3></Id_Number3><Surname>曾辉</Surname>"
                              + "<Sex></Sex><Date_Of_Birth></Date_Of_Birth><Home_Address2></Home_Address2><Home_Address3></Home_Address3>"
                              + "<Home_Phone_Number></Home_Phone_Number><Mobile_Phone_Number>13882377732</Mobile_Phone_Number><User_Field1>紧急联系人</User_Field1>"
                              + "<User_Field3></User_Field3><User_Field6></User_Field6></Guarantor><Valuer><First_Name>信用卡审批</First_Name>"
                              + "<User_Field1>主要申请人</User_Field1><User_Field2></User_Field2><User_Field5>28/02/2017</User_Field5></Valuer>"
                              + "<Valuer><First_Name>信用卡审批</First_Name><User_Field1>主要申请人</User_Field1><User_Field2></User_Field2>"
                              + "<User_Field5>28/02/2017</User_Field5></Valuer><User><Company_Name>攀枝花市达瑞堂健康服务部</Company_Name>"
                              + "<Company_Address1>四川省攀枝花市东区瓜子坪村</Company_Address1>"
                              + "<User_Field1>中级领导（行政级别局级以下领导或大公司中级管理人员）</User_Field1><User_Field2>批发和零售业</User_Field2>"
                              + "<User_Field5>01/03/2017</User_Field5><User_Field6>--</User_Field6><User_Field7>2014</User_Field7>"
                              + "<User_Field8>主要申请人</User_Field8></User><User><Company_Name>货运</Company_Name><Company_Address1>--</Company_Address1>"
                              + "<User_Field1>--</User_Field1><User_Field2>租赁和商务服务业</User_Field2><User_Field5>25/05/2011</User_Field5>"
                              + "<User_Field6>商业、服务业人员</User_Field6><User_Field7>2005</User_Field7><User_Field8>主要申请人</User_Field8></User><User>"
                              + "<Company_Name>攀枝花市达瑞堂健康服务部</Company_Name><Company_Address1>四川省攀枝花市东区瓜子坪村</Company_Address1>"
                              + "<User_Field1>中级领导（行政级别局级以下领导或大公司中级管理人员）</User_Field1><User_Field2>批发和零售业</User_Field2>"
                              + "<User_Field5>01/03/2017</User_Field5><User_Field6>--</User_Field6><User_Field7>2014</User_Field7><User_Field8>主要申请人</User_Field8>"
                              + "</User><User><Company_Name>货运</Company_Name><Company_Address1>--</Company_Address1><User_Field1>--</User_Field1>"
                              + "<User_Field2>租赁和商务服务业</User_Field2><User_Field5>25/05/2011</User_Field5><User_Field6>商业、服务业人员</User_Field6>"
                              + "<User_Field7>2005</User_Field7><User_Field8>主要申请人</User_Field8></User><CreditBureau><Id_Number1>--</Id_Number1>"
                              + "<Id_Number2>--</Id_Number2><Surname>--</Surname><Date_Of_Birth>25/10/1977</Date_Of_Birth><Sex>M</Sex>"
                              + "<Home_Address1>四川省攀枝花市西区清香坪路南社区22号12栋1单元1号</Home_Address1><Home_Address2>四川省攀枝花市西区建兴路22号6栋3单元12号</Home_Address2>"
                              + "<Home_Address3>西区清香坪路南社区22号12栋1单元1号</Home_Address3><Home_Address4>四川省攀枝花市西区建兴路22号6栋3单元12号</Home_Address4>"
                              + "<Home_Address5></Home_Address5><Home_Phone_Number>--</Home_Phone_Number><Office_Phone_Number>08122231587</Office_Phone_Number>"
                              + "<Mobile_Phone_Number>18784370370</Mobile_Phone_Number><Other_Phone_Number>--</Other_Phone_Number><Company_Name>--</Company_Name>"
                              + "<User_Field1>主要申请人</User_Field1><User_Field2>高中</User_Field2><User_Field3>--</User_Field3><User_Field4>已婚</User_Field4>"
                              + "<User_Field21>01/03/2017</User_Field21><User_Field22>25/05/2011</User_Field22><User_Field23></User_Field23></CreditBureau>"
                              + "<CreditBureau><Id_Number1>--</Id_Number1><Id_Number2>--</Id_Number2><Surname>--</Surname><Date_Of_Birth>25/10/1977</Date_Of_Birth>"
                              + "<Sex>M</Sex><Home_Address1>四川省攀枝花市西区清香坪路南社区22号12栋1单元1号</Home_Address1>"
                              + "<Home_Address2>四川省攀枝花市西区建兴路22号6栋3单元12号</Home_Address2><Home_Address3>西区清香坪路南社区22号12栋1单元1号</Home_Address3>"
                              + "<Home_Address4>四川省攀枝花市西区建兴路22号6栋3单元12号</Home_Address4><Home_Address5></Home_Address5><Home_Phone_Number>--</Home_Phone_Number>"
                              + "<Office_Phone_Number>08122231587</Office_Phone_Number><Mobile_Phone_Number>18784370370</Mobile_Phone_Number>"
                              + "<Other_Phone_Number>--</Other_Phone_Number><Company_Name>--</Company_Name><User_Field1>主要申请人</User_Field1>"
                              + "<User_Field2>高中</User_Field2><User_Field3>--</User_Field3><User_Field4>已婚</User_Field4><User_Field21>01/03/2017</User_Field21>"
                              + "<User_Field22>25/05/2011</User_Field22><User_Field23></User_Field23></CreditBureau><Loan><Loan_Id>123456789</Loan_Id></Loan>"
                              + "</Application></ApplicationSchema>";
            String ret = (String) call.invoke(new Object[] { postdata });
            System.out.println("Successful = " + ret);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
