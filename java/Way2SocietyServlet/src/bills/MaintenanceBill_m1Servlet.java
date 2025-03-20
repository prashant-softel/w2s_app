package bills;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MaintenanceBill_m1Servlet
 */
@WebServlet("/MaintenanceBill_m1Servlet")
public class MaintenanceBill_m1Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MaintenanceBill_m1Servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}


/*

require_once("java/Java.inc");

$objLogin = new Java("MainClasses.MaintenanceBill",$_REQUEST['token'],TRUE,$_REQUEST['tkey']);

if(java_values($objLogin->IsTokenValid()) == true)
{
	$DecryptedTokenMap = java_values($objLogin->getDecryptedTokenMap());
	$unit_id = $DecryptedTokenMap['unit_id'];
	$data = $objLogin->mfetchBillNReceiptsDetails($unit_id);
	 $tmpResult  = sortArray(java_values($data["response"]["BillnReceipts"]),"sdate");
	 //array_sort_by_column($data["response"]["BillnReceipts"],"Date");
	 //usort($data["response"]["BillnReceipts"], "sortFunction");
	 $data["response"]["BillnReceipts"] =  $tmpResult;
	echo json_encode(java_values($data));
}
else
{
	$result = array("success"=>0 , "response"=> array("message" => "Invalid Token"));
	echo json_encode($result);
}


function sortArray($array,$inputKey)
{
	foreach($array as $key=>$value){
        $arr_Ref[$key] = $array[$key][$inputKey];
        }
	array_multisort($arr_Ref, SORT_ASC,$array);	
	return $array;	
}


function array_sort_by_column(&$array, $column, $direction = SORT_ASC) {
    $reference_array = array();

    foreach($array as $key => $row) {
        $reference_array[$key] = $row[$column];
    }

    array_multisort($reference_array, SORT_DESC, SORTDATE,$array);
}

function sortFunction( $a, $b) {
    return strtotime($a["Date"]) - strtotime($b["Date"]);
}



?>
*/