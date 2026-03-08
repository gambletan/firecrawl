export const PDF_SNIFF_WINDOW = 1024;

const PDF_MAGIC = Buffer.from("%PDF");

// Common HTML signatures to detect HTML content masquerading as PDF
const HTML_SIGNATURES = [
  Buffer.from("<!DOCTYPE html"),
  Buffer.from("<!doctype html"),
  Buffer.from("<html"),
  Buffer.from("<HTML"),
];

/** Check if a buffer contains the %PDF magic bytes within the first 1KB. */
export function isPdfBuffer(buf: Buffer): boolean {
  const window = buf.subarray(0, Math.min(buf.length, PDF_SNIFF_WINDOW));
  return window.includes(PDF_MAGIC);
}

/** Check if a buffer contains HTML content (which may indicate a webpage with embedded PDF). */
export function isHtmlBuffer(buf: Buffer): boolean {
  const window = buf.subarray(0, Math.min(buf.length, PDF_SNIFF_WINDOW));
  return HTML_SIGNATURES.some(sig => window.includes(sig));
}
