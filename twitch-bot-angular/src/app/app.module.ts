import { AppRoutingModule } from './routing/app-routing.module';
import { HeaderComponent } from './header/header.component';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { CommandsComponent } from './commands/commands.component';
import { CommandListComponent } from './commands/command-list/command-list.component';
import { CommandDetailComponent } from './commands/command-detail/command-detail.component';
import { CommandListItemComponent } from './commands/command-list/command-list-item/command-list-item.component';
import { StatusComponent } from './status/status.component';
import { CommandStartComponent } from './commands/command-start/command-start.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    CommandsComponent,
    CommandListComponent,
    CommandDetailComponent,
    CommandListItemComponent,
    StatusComponent,
    CommandStartComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
