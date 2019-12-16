import { Component } from "@angular/core";
import { map, debounceTime } from "rxjs/operators";
import { webSocket, WebSocketSubject } from "rxjs/webSocket";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"]
})
export class AppComponent {
  buttonTitle = "";
  buttonType = "";
  connected = false;
  myWebSocket: WebSocketSubject<any> = webSocket(
    "ws://localhost:8082/websocket"
  );

  displayMessages: any[] = [];
  socketData = [];
  temp = [
    {
      type: "[Kafka] Add message",
      stationId: 95,
      temperature: 11,
      city: "California"
    }
  ];
  constructor() {}

  ngOnInit() {
    this.handleButtonTitle();
  }

  onButtonClick() {
    if (this.connected) {
      this.closeWebSocket();
    } else {
      this.readFromWebsocket();
    }
    this.connected = !this.connected;
    this.handleButtonTitle();
  }

  private readFromWebsocket() {
    this.myWebSocket
      .asObservable()
      .pipe(
        map(data => {
          const jsonData = JSON.parse(data);
          this.socketData.unshift(jsonData);
        }),
        debounceTime(200)
      )
      .subscribe(msg => {
        console.log(this.socketData);
        
        this.displayMessages = this.socketData.slice(0, 5);
      });
  }

  private closeWebSocket() {
    this.myWebSocket.complete();
  }

  private handleButtonTitle() {
    if (this.connected) {
      this.buttonTitle = "Disconnect";
      this.buttonType = "danger";
    } else {
      this.buttonTitle = "Connect";
      this.buttonType = "primary";
    }
  }
}
