import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, NavController, NavParams, Platform } from '@ionic/angular';
import { GlobalVars } from 'src/service/globalvars';
import { LoaderView } from 'src/service/loaderview';
import { ConnectServer } from 'src/service/connectserver';
import { NavigationExtras } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
//import { Chart,registerables  } from 'chart.js';
//import Chart from 'chart.js/auto';
import { Chart, BarElement, BarController, CategoryScale, Decimation, Filler, Legend, Title, Tooltip, registerables} from 'chart.js';
;
@Component({
  selector: 'app-takepoll',
  templateUrl: './takepoll.page.html',
  styleUrls: ['./takepoll.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule],
  
})
export class TakepollPage implements OnInit {
  @ViewChild('barCanvas') barCanvas;
  
  barChart: any;
  voted :number =0;
  options : Array<any>;
  option_id: any;
  group_id:number;
  poll_id:number;
  polls:Array<any>;
  options_array:Array<any>;
  society_id:number;
  start_date:string;
  end_date:string;
  poll_status:number;
  question:string;
  allow_vote:number;
  additional_content:string;
  labels:Array<any>;
  
  counter:Array<any>;
  old_option_id: number;
  user_option:number;
  details:Array<any>;
  flag:number;
  selected_vote:string;
  totalVote: number;
  VoteCounter:number;
  active:any;
  Revote:number;
  ReviewFlag:any;
  ReviewQuestion:any;
  member_Comment:any;
  CheckcommentStatus:boolean;
  ShowTextarea:any;
  AddedReview:any;
  constructor(private navCtrl: NavController,
    private globalVars: GlobalVars,
    private connectServer: ConnectServer,
    private platform: Platform,
    private loaderView: LoaderView,
    private params: NavParams,
    private route: ActivatedRoute) {
		
    this.options=[];
		this.option_id=0;
		this.old_option_id=0;
		this.options_array=[];
		this.group_id=0;
		this.society_id=0;
		this.start_date="00-00-0000";
		this.end_date= "00-00-0000";
		this.poll_status= 0;
		this.question= "";
		this.allow_vote= 0;
		this.additional_content="";
		this.labels=[];
		this.counter=[];
		this.details=[];
		this.user_option = 0;
		this.flag=0;
		this.selected_vote="";
		this.totalVote=0;  
		this.VoteCounter=0;
		this.active=true;
		this.Revote = 0;
		this.ReviewFlag =0;
		this.ReviewQuestion="";
		this.member_Comment="";
		this.CheckcommentStatus=false;
		this.ShowTextarea = "0";
		this.AddedReview="";
		Chart.register(...registerables);
		//Chart.register(BarElement, BarController, CategoryScale, Decimation, Filler, Legend, Title, Tooltip);
    
   }

  ngOnInit() {
    let details:any;
    this.route.queryParams.subscribe(params => {
      details = params["details"];
        
      });
      //console.log(details);
      this.displayData(details);
  }
	
	displayData(details)
 	{
 		this.details=details;
 		//console.log("take poll" ,this.details);
 		this.group_id=this.details['group_id'];
 		this.poll_id=this.details['poll_id'];
 		this.society_id=this.details['society_id'];
 		this.active = this.details['active'];
 		//alert(this.active );
 		this.totalVote = 0;
 		this.Revote = 0;
 		var obj = [];
		obj['fetch'] = 2;
    	obj['group_id']=this.group_id;
		obj['poll_id']=this.poll_id;
		obj['society_id']=this.society_id;
		this.connectServer.getData("Polls", obj).then(
			resolve => { 
		        this.loaderView.dismissLoader();
		       	if(resolve['success'] == 1)
		  	    {
		  	       	this.polls = resolve['response']['vote'];
		  	        this.options_array = resolve['response']['options'];
		  	        this.option_id=resolve['response']['options'][0]['option_id'];
                	this.old_option_id=this.option_id;
		  	        this.start_date=this.details['start_date'];
		  	        this.end_date=this.details['end_date'];
					this.poll_id=this.details['poll_id'];
		  	        this.poll_status=this.details['poll_status'];
		  	        this.question=this.details['question'];
		  	        this.allow_vote=this.details['allow_vote'];
		  	        this.additional_content=this.details['additional_content'];
		  	        this.ReviewFlag=this.details['comment_flag'];
		  	        this.ReviewQuestion=this.details['comment_question'];
		  	        document.getElementById("additional_text").innerHTML = this.additional_content;
		  	        if(this.polls.length > 0)
		  	        {
		  	            this.user_option  = this.polls[0]['option_id'];
		  	            this.selected_vote=this.polls[0]['yourvote'];
		  	            this.option_id = this.user_option;
		  	           	this.old_option_id = this.option_id;
		  	           	this.AddedReview=this.polls[0]['yourReview'];
		  	           	this.member_Comment=this.polls[0]['yourReview'];
		  	        }
		  	        else
		  	        {
		  	            this.user_option  = 0;
		  	            this.option_id = 0;
		  	            document.getElementById("Disribution_vote").style.display='block';
		  	        }
					if(this.user_option !=0 || this.allow_vote ==1)
		  	       	{
		  	            document.getElementById("Disribution_vote").style.display='block';
		  	        }
					console.log("option array" , this.options_array);
		  	      	for(var i=0;i<this.options_array.length;i++)
		  	        {
		  	            this.labels[i]=this.options_array[i]['options'];
		  	            this.counter[i]=this.options_array[i]['counter'];
		  	            this.totalVote +=Number(this.counter[i]);
		  	        }
		  	        this.barChart = new Chart(this.barCanvas.nativeElement, {
    		       	type: 'bar',
                    data: {
                            labels: this.labels,
                            datasets:
                            [{
                                label: this.labels.toString(),//this.labels,
                                data: this.counter,
                                backgroundColor: 
                                [
                                    'rgba(255, 99, 132, 0.2)',
                                    'rgba(54, 162, 235, 0.2)',
                                    'rgba(255, 206, 86, 0.2)',
                                    'rgba(75, 192, 192, 0.2)',
                                    'rgba(153, 102, 255, 0.2)',
                                    'rgba(255, 159, 64, 0.2)',
                                    'rgba(255, 69, 132, 0.2)',
                                    'rgba(54, 62, 235, 0.2)',
                                    'rgba(25, 206, 86, 0.2)',
                                    'rgba(75, 92, 192, 0.2)',
                                    'rgba(153, 12, 255, 0.2)',
                                    'rgba(255, 19, 64, 0.2)'
                                ],
                                borderColor:
                                [
                                    'rgba(255,99,132,1)',
                                    'rgba(54, 162, 235, 1)',
                                    'rgba(255, 206, 86, 1)',
                                    'rgba(75, 192, 192, 1)',
                                    'rgba(153, 102, 255, 1)',
                                    'rgba(255, 159, 64, 1)',
                                    'rgba(255, 69, 132, 1)',
                                    'rgba(54, 62, 235, 1)',
                                    'rgba(25, 206, 86, 1)',
                                    'rgba(75, 92, 192, 1)',
                                    'rgba(153, 12, 255, 1)',
                                    'rgba(255, 19, 64, 1)'
                                ],
                                borderWidth: 1
                		    }]
            		    },
            			options: 
            			{
                            scales: 
                            {
                               /*	yAxes: 
                               	[{
                                    ticks: 
                                    {
                                       beginAtZero:true
                        		    }
                    		    }]*/
                		    }
            			}
 					}
 				);
		  	                  	
			}
		  	                  	
		}
	);
		    
}
		
insertvote()
{
	if(this.option_id == 0)
 	{
 		alert("Please Select Vote Option!");
 	}
 	else
 	{
		let details:any;
    	this.route.queryParams.subscribe(params => {
      		details = params["details"];
     	});
    	this.details=details;//this.navParams.get("details");
 		this.group_id=this.details['group_id'];
 		this.poll_id=this.details['poll_id'];
 		this.society_id=this.details['society_id'];
		var obj = [];
 		obj['set'] = 1;
		obj['group_id']=this.group_id;
		obj['poll_id']=this.poll_id;
		obj['society_id']=this.society_id;
		obj['option_id']=this.option_id;
		obj['mem_review']=this.member_Comment;
		this.connectServer.getData("Polls", obj).then(
			resolve => { 
		      	this.loaderView.dismissLoader();
		        if(resolve['success'] == 1)
		  	    {
		  	       // this.displayData(this.navParams.get("details"));
                	this.displayData(details);
		  	    }
		  	}
		);
	}
}
RevoteOption() {
	this.Revote = 1;
  }

  cancelRevoteOption() {
	this.Revote = 0;
  }

  updatevote()
  { 
	let details:any;
    this.route.queryParams.subscribe(params => {
      		details = params["details"];
    });
 	this.details=details;//this.navParams.get("details");
 	this.poll_id=this.details['poll_id'];
 	this.society_id=this.details['society_id'];
 	var obj = [];
 	obj['set'] = 2;
	obj['poll_id']=this.poll_id;
	obj['society_id']=this.society_id;
	obj['option_id']=this.option_id;
	obj['old_option_id']=this.old_option_id;
	obj['mem_review']=this.member_Comment;
	this.connectServer.getData("Polls", obj).then(
		 resolve => { 
		    this.loaderView.dismissLoader();
		    if(resolve['success'] == 1)
		  	{
		  	    this.displayData(this.details);
				//this.displayData(this.navParams.get("details"));
		  	}
		}
	);
}
  
}
