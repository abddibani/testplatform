import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'client',
        data: { pageTitle: 'platformApp.client.home.title' },
        loadChildren: () => import('./client/client.module').then(m => m.ClientModule),
      },
      {
        path: 'customer',
        data: { pageTitle: 'platformApp.customer.home.title' },
        loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule),
      },
      {
        path: 'exchange',
        data: { pageTitle: 'platformApp.exchange.home.title' },
        loadChildren: () => import('./exchange/exchange.module').then(m => m.ExchangeModule),
      },
      {
        path: 'order',
        data: { pageTitle: 'platformApp.order.home.title' },
        loadChildren: () => import('./order/order.module').then(m => m.OrderModule),
      },
      {
        path: 'tracker',
        data: { pageTitle: 'platformApp.tracker.home.title' },
        loadChildren: () => import('./tracker/tracker.module').then(m => m.TrackerModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
