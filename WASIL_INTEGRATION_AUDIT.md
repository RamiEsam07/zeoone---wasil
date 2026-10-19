# WASIL Admin App — Integration Audit

**Audit branch:** `ops/wasil-integration-audit`  
**Base:** `fix/wasil-supabase-auth-review`  
**Production status:** Not approved for release. `main` is unchanged.

## Verified in repository
- Android admin app uses `SupabaseRoleAuthenticator(context, "admin")` and checks the server-owned profile role/active status.
- Admin view-model is connected to screens for dashboard, orders/details, drivers, stores, users, products, categories, profile, and settings.
- The entry flow invokes Supabase login and refreshes read-focused screens without intentionally refreshing over an in-progress edit form.
- Build configuration reads Supabase URL, publishable key, and Google Web OAuth client ID from local properties. Never embed a service-role/secret key in the client.

## Findings requiring follow-up
- Fixed in the audit branch (pending validation/deployment): `updateStore` now calls the atomic `wasil_admin_update_store` RPC instead of separate PATCH + approval requests. The matching draft migration is `202610170008_admin_update_store_atomic.sql` in the customer repository. The migration and admin app change must be reviewed and released together; the RPC has not been applied to live Supabase.
- The admin UI must treat every repository result as fallible and display failure feedback; a successful-looking form must not imply that a rejected RLS/RPC operation was saved.

## Not yet verified
- Android build/test result for this audit branch.
- Live OAuth and actual admin-role provisioning.
- Each admin write action against database grants/RLS/RPCs, especially store approval, account activation, product/category changes, order status, and driver assignment.
- Audit trail, confirmation safeguards for destructive actions, and full order lifecycle across all apps.

## Release gates
1. Run Android build and unit tests for this branch.
2. Test non-admin and inactive accounts are rejected even if they can sign in with Google.
3. Verify every privileged operation is enforced by database/RPC authorization, not merely hidden UI controls.
4. Ensure destructive actions require confirmation and return clear success/failure feedback.
5. Review findings before merge. No live migration or merge to `main` has been performed by this audit.
