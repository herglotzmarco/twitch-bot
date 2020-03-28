import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  menuCollapsed = true;

  @Output() featureSelected = new EventEmitter<string>();

  onSelectFeature(selectedFeature: string) {
    this.featureSelected.emit(selectedFeature);
  }

}
