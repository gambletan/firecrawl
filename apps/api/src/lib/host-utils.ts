import { type Request } from "express";
import { config } from "../config";

/**
 * Get the hostname for self-hosted instances.
 * Returns the configured SELF_HOSTED_DOMAIN if set, otherwise falls back to req.get("host").
 * This is useful when running behind a reverse proxy.
 */
export function getHostname(req: Request): string {
  return config.SELF_HOSTED_DOMAIN ?? req.get("host") ?? "localhost";
}
