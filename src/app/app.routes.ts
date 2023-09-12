import { Routes } from '@angular/router';
//
export const routes: Routes = [
  // {
  //   path: 'home',
  //   loadComponent: () => import('./home/home.page').then((m) => m.HomePage),
  // },
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  {
    path: 'login',
    loadComponent: () => import('./login/login.page').then(m => m.LoginPage)
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./dashboard/dashboard').then(m => m.Dashboard)
  },
  {
    path: 'LoginPage',
    loadComponent: () => import('./login/login.page').then(m => m.LoginPage)
  },
  {
    path: 'Dashboard',
    loadComponent: () => import('./dashboard/dashboard').then(m => m.Dashboard)
  },
  {
    path: 'AboutusPage',
    loadComponent: () => import('./aboutus/aboutus').then(m => m.AboutusPage)
  },
  {
    path: 'ContectPage',
    loadComponent: () => import('./contect/contect').then(m => m.ContectPage)
  },
  {
    path: 'Feedback',
    loadComponent: () => import('./feedback/feedback').then(m => m.Feedback)
  },
  {
    path: 'Home',
    loadComponent: () => import('./home/home').then(m => m.Home)
  },
  {
    path: 'Survey',
    loadComponent: () => import('./survey/survey').then(m => m.Survey)
  },
  {
    path: 'SurveyhistoryPage',
    loadComponent: () => import('./surveyhistory/surveyhistory').then(m => m.SurveyhistoryPage)
  },
  {
    path: 'UpdatesurveyPage',
    loadComponent: () => import('./updatesurvey/updatesurvey').then(m => m.UpdatesurveyPage)
  },
  {
    path: 'ViewRemindersPage',
    loadComponent: () => import('./viewreminders/viewreminders').then(m => m.ViewRemindersPage)
  },
  {
    path: 'ViewsurveyPage',
    loadComponent: () => import('./viewsurvey/viewsurvey').then(m => m.ViewsurveyPage)
  },
  // {
  //   path: 'documents',
  //   loadComponent: () => import('./documents/documents.page').then(m => m.DocumentsPage)
  // },
  // {
  //   path: 'dues',
  //   loadComponent: () => import('./dues/dues.page').then(m => m.DuesPage)
  // },
  // {
  //   path: 'events',
  //   loadComponent: () => import('./events/events.page').then(m => m.EventsPage)
  // },
  // {
  //   path: 'features',
  //   loadComponent: () => import('./features/features.page').then(m => m.FeaturesPage)
  // },
  // {
  //   path: 'fine',
  //   loadComponent: () => import('./fine/fine.page').then(m => m.FinePage)
  // },
  // {
  //   path: 'fineimageview',
  //   loadComponent: () => import('./fineimageview/fineimageview.page').then(m => m.FineimageviewPage)
  // },
  // {
  //   path: 'helpline',
  //   loadComponent: () => import('./helpline/helpline.page').then(m => m.HelplinePage)
  // },
  // {
  //   path: 'imageview',
  //   loadComponent: () => import('./imageview/imageview.page').then(m => m.ImageviewPage)
  // },
  // {
  //   path: 'imposedetail',
  //   loadComponent: () => import('./imposedetail/imposedetail.page').then(m => m.ImposedetailPage)
  // },
  // {
  //   path: 'member',
  //   loadComponent: () => import('./member/member.page').then(m => m.MemberPage)
  // },
  // {
  //   path: 'newuser',
  //   loadComponent: () => import('./newuser/newuser.page').then(m => m.NewuserPage)
  // },
  // {
  //   path: 'notices',
  //   loadComponent: () => import('./notices/notices.page').then(m => m.NoticesPage)
  // },
  // {
  //   path: 'payment',
  //   loadComponent: () => import('./payment/payment.page').then(m => m.PaymentPage)
  // },
  // {
  //   path: 'photoalbum',
  //   loadComponent: () => import('./photoalbum/photoalbum.page').then(m => m.PhotoalbumPage)
  // },
  // {
  //   path: 'poll',
  //   loadComponent: () => import('./poll/poll.page').then(m => m.PollPage)
  // },
  // {
  //   path: 'providerdetails',
  //   loadComponent: () => import('./providerdetails/providerdetails.page').then(m => m.ProviderdetailsPage)
  // },
  // {
  //   path: 'receipts',
  //   loadComponent: () => import('./receipts/receipts.page').then(m => m.ReceiptsPage)
  // },
  // {
  //   path: 'recordneft',
  //   loadComponent: () => import('./recordneft/recordneft.page').then(m => m.RecordneftPage)
  // },
  // {
  //   path: 'report-photo',
  //   loadComponent: () => import('./report-photo/report-photo.page').then(m => m.ReportPhotoPage)
  // },
  // {
  //   path: 'review',
  //   loadComponent: () => import('./review/review.page').then(m => m.ReviewPage)
  // },
  // {
  //   path: 'serviceprovider',
  //   loadComponent: () => import('./serviceprovider/serviceprovider.page').then(m => m.ServiceproviderPage)
  // },
  // {
  //   path: 'serviceproviederimageview',
  //   loadComponent: () => import('./serviceproviederimageview/serviceproviederimageview.page').then(m => m.ServiceproviederimageviewPage)
  // },
  // {
  //   path: 'servicerequest',
  //   loadComponent: () => import('./servicerequest/servicerequest.page').then(m => m.ServicerequestPage)
  // },
  // {
  //   path: 'servicerequestimageview',
  //   loadComponent: () => import('./servicerequestimageview/servicerequestimageview.page').then(m => m.ServicerequestimageviewPage)
  // },
  // {
  //   path: 'settings',
  //   loadComponent: () => import('./settings/settings.page').then(m => m.SettingsPage)
  // },
  // {
  //   path: 'society',
  //   loadComponent: () => import('./society/society.page').then(m => m.SocietyPage)
  // },
  // {
  //   path: 'takepoll',
  //   loadComponent: () => import('./takepoll/takepoll.page').then(m => m.TakepollPage)
  // },
  // {
  //   path: 'task',
  //   loadComponent: () => import('./task/task.page').then(m => m.TaskPage)
  // },
  // {
  //   path: 'tenants',
  //   loadComponent: () => import('./tenants/tenants.page').then(m => m.TenantsPage)
  // },
  // {
  //   path: 'tenantsdetails',
  //   loadComponent: () => import('./tenantsdetails/tenantsdetails.page').then(m => m.TenantsdetailsPage)
  // },
  // {
  //   path: 'updatefine',
  //   loadComponent: () => import('./updatefine/updatefine.page').then(m => m.UpdatefinePage)
  // },
  // {
  //   path: 'updateprofile',
  //   loadComponent: () => import('./updateprofile/updateprofile.page').then(m => m.UpdateprofilePage)
  // },
  // {
  //   path: 'updateprovider',
  //   loadComponent: () => import('./updateprovider/updateprovider.page').then(m => m.UpdateproviderPage)
  // },
  // {
  //   path: 'updatetask',
  //   loadComponent: () => import('./updatetask/updatetask.page').then(m => m.UpdatetaskPage)
  // },
  // {
  //   path: 'vehicle',
  //   loadComponent: () => import('./vehicle/vehicle.page').then(m => m.VehiclePage)
  // },
  // {
  //   path: 'viewbill',
  //   loadComponent: () => import('./viewbill/viewbill.page').then(m => m.ViewbillPage)
  // },
  // {
  //   path: 'viewevent',
  //   loadComponent: () => import('./viewevent/viewevent.page').then(m => m.VieweventPage)
  // },
  // {
  //   path: 'viewfeature',
  //   loadComponent: () => import('./viewfeature/viewfeature.page').then(m => m.ViewfeaturePage)
  // },
  // {
  //   path: 'viewimage',
  //   loadComponent: () => import('./viewimage/viewimage.page').then(m => m.ViewimagePage)
  // },
  // {
  //   path: 'viewimposefine',
  //   loadComponent: () => import('./viewimposefine/viewimposefine.page').then(m => m.ViewimposefinePage)
  // },
  // {
  //   path: 'viewnotice',
  //   loadComponent: () => import('./viewnotice/viewnotice.page').then(m => m.ViewnoticePage)
  // },
  // {
  //   path: 'viewreceipt',
  //   loadComponent: () => import('./viewreceipt/viewreceipt.page').then(m => m.ViewreceiptPage)
  // },
  // {
  //   path: 'viewservicerequest',
  //   loadComponent: () => import('./viewservicerequest/viewservicerequest.page').then(m => m.ViewservicerequestPage)
  // },
  // {
  //   path: 'visitor-in',
  //   loadComponent: () => import('./visitor-in/visitor-in.page').then(m => m.VisitorInPage)
  // },
  // {
  //   path: 'cnote',
  //   loadComponent: () => import('./cnote/cnote.page').then( m => m.CnotePage)
  // },
  // {
  //   path: 'linkflat',
  //   loadComponent: () => import('./linkflat/linkflat.page').then( m => m.LinkflatPage)
  // },
  // {
  //   path: 'addservicerequest',
  //   loadComponent: () => import('./addservicerequest/addservicerequest.page').then( m => m.AddservicerequestPage)
  // },
  // {
  //   path: 'services',
  //   loadComponent: () => import('./services/services.page').then( m => m.ServicesPage)
  // },
  // {
  //   path: 'myvisitors',
  //   loadComponent: () => import('./myvisitors/myvisitors.page').then( m => m.MyvisitorsPage)
  // },
  // {
  //   path: 'addtask',
  //   loadComponent: () => import('./addtask/addtask.page').then(m => m.AddTaskPage)
  // },
  // {
  //   path: 'addtenant',
  //   loadComponent: () => import('./addtenant/addtenant.page').then(m => m.AddTenantPage)
  // },
  // {
  //   path: 'addprovider',
  //   loadComponent: () => import('./addprovider/addprovider.page').then(m => m.AddProviderPage)
  // },
];