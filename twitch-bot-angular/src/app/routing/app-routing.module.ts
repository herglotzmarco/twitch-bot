import { CommandStartComponent } from './../commands/command-start/command-start.component';
import { CommandDetailComponent } from './../commands/command-detail/command-detail.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { StatusComponent } from './../status/status.component';
import { CommandsComponent } from './../commands/commands.component';


export const routes: Routes = [
    { path: '', pathMatch: 'full', redirectTo: '/status' },
    { path: 'status', component: StatusComponent },
    {
        path: 'commands', component: CommandsComponent, children: [
            { path: '', component: CommandStartComponent },
            { path: ':name', component: CommandDetailComponent },
        ]
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
