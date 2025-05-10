import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class LocalStorageService {

  /**
   * ✅ Safely get item from localStorage
   */
  getItem(key: string): string | null {
    if (this.isBrowser()) {
      return localStorage.getItem(key);
    }
    return null;
  }

  /**
   * ✅ Safely set item in localStorage
   */
  setItem(key: string, value: string): void {
    if (this.isBrowser()) {
      localStorage.setItem(key, value);
    }
  }

  /**
   * ✅ Safely remove item from localStorage
   */
  removeItem(key: string): void {
    if (this.isBrowser()) {
      localStorage.removeItem(key);
    }
  }

  /**
   * ✅ Clear all localStorage data
   */
  clear(): void {
    if (this.isBrowser()) {
      localStorage.clear();
    }
  }

  /**
   * ✅ Check if running in browser context
   */
  private isBrowser(): boolean {
    return typeof window !== 'undefined' && typeof localStorage !== 'undefined';
  }
}
