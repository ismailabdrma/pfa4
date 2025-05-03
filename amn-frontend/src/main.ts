import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import {
  provideHttpClient,
  withFetch,
  withInterceptors
} from '@angular/common/http';
import { JwtInterceptor } from './app/interceptors/jwt.interceptor';
import { appConfig } from './app/app.config';

bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(
      withFetch(),
      withInterceptors([JwtInterceptor])
    ),
    ...(appConfig.providers || [])
  ]
}).catch((err) => console.error(err));
