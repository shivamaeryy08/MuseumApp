import Vue from 'vue'
import Router from 'vue-router'
import ArtworkLayout from '../components/ArtworkLayout'
import MuseumInformation from '@/components/MuseumInformation'
import MainHome from '@/components/MainHome'
import ShiftScheduleViewer from '@/components/ShiftScheduleViewer'
import AdminEmployeeManager from '@/components/AdminEmployeeManager'
import AdminShiftManager from '@/components/AdminShiftManager'
import Home from '@/components/Home'
import OrderInformation from '@/components/OrderInformation'
import LoanRequestManager from '@/components/LoanRequestManager'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'MainHome',
      component: MainHome
    },
    {
      path: '/museuminformation',
      name: 'MuseumInformation',
      component: MuseumInformation
    },
    {
      path: '/employeemanager',
      name: 'AdminEmployeeManager',
      component: AdminEmployeeManager
    },
    {
      path: '/shiftscheduler',
      name: 'AdminShiftScheduler',
      component: AdminShiftManager
    },
    {
      path: '/scheduleviewer',
      name: 'ShiftScheduleViewer',
      component: ShiftScheduleViewer
    },
    {
      path: '/orderinformation',
      name: 'OrderInformation',
      component: OrderInformation
    },
    {
      path: '/viewloanrequests',
      name: 'LoanRequestManager',
      component: LoanRequestManager
    },
    {
      path: '/home',
      name: 'Home',
      component: Home
    }, 
    {
      path: '/artworklayout',
      name: 'ArtworkLayout',
      component: ArtworkLayout
    },
  ]
})
