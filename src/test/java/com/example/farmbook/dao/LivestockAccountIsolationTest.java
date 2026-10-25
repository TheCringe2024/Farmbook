package com.example.farmbook.dao;

import com.example.farmbook.SessionState;
import com.example.farmbook.model.Livestock;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LivestockAccountIsolationTest {

    @Test
    void accountBCannotAccessAccountARecords() {
        String unique = UUID.randomUUID().toString();
        String ownerA = "test-user-A-" + unique;
        String ownerB = "test-user-B-" + unique;
        String identifier = "FR13-" + unique;
        LivestockDAO accountA = new LivestockDAO(ownerA);
        LivestockDAO accountB = new LivestockDAO(ownerB);
        int animalId = -1;

        try {
            assertTrue(accountA.save(
                    new Livestock("Cow", identifier, "2026-10-25")));
            Livestock saved = accountA.findAll().stream()
                    .filter(a -> identifier.equals(a.getIdentifier()))
                    .findFirst().orElseThrow();
            animalId = saved.getId();
            final int savedId = animalId;

            assertTrue(accountB.findAll().stream()
                    .noneMatch(a -> a.getId() == savedId));

            Livestock unauthorizedUpdate = new Livestock(
                    "Sheep", identifier, "2026-10-25");
            unauthorizedUpdate.setId(savedId);
            assertFalse(accountB.update(unauthorizedUpdate));
            assertFalse(accountB.delete(savedId));

            Livestock unchanged = accountA.findAll().stream()
                    .filter(a -> a.getId() == savedId)
                    .findFirst().orElseThrow();
            assertEquals("Cow", unchanged.getSpecies());

            Livestock authorizedUpdate = new Livestock(
                    "Goat", identifier, "2026-10-25");
            authorizedUpdate.setId(savedId);
            assertTrue(accountA.update(authorizedUpdate));
            assertEquals("Goat", accountA.findAll().stream()
                    .filter(a -> a.getId() == savedId)
                    .findFirst().orElseThrow().getSpecies());

            assertTrue(accountA.delete(savedId));
            animalId = -1;
        } finally {
            if (animalId > 0) {
                accountA.delete(animalId);
            }
        }
    }

    @Test
    void daoMustRejectAccessAfterLogout() {
        String owner = "session-A-" + UUID.randomUUID();
        String identifier = "FR13-SESSION-" + UUID.randomUUID();
        SessionState.login(owner);
        LivestockDAO dao = new LivestockDAO();
        int animalId = -1;

        try {
            assertTrue(dao.save(new Livestock(
                    "Cow", identifier, "2026-10-25")));
            animalId = dao.findAll().stream()
                    .filter(a -> identifier.equals(a.getIdentifier()))
                    .findFirst().orElseThrow().getId();

            SessionState.logout();
            assertTrue(dao.findAll().isEmpty(),
                    "DAO must not read records after logout");
            assertFalse(dao.delete(animalId));
            Livestock changed = new Livestock(
                    "Sheep", identifier, "2026-10-25");
            changed.setId(animalId);
            assertFalse(dao.update(changed));
            assertFalse(dao.save(new Livestock(
                    "Goat", "UNAUTH-" + UUID.randomUUID(), "2026-10-25")));

            SessionState.login("session-B-" + UUID.randomUUID());
            assertTrue(dao.findAll().isEmpty());
            assertFalse(dao.delete(animalId));
        } finally {
            SessionState.logout();
            if (animalId > 0) {
                new LivestockDAO(owner).delete(animalId);
            }
        }
    }

    @Test
    void oldDaoMustStayInvalidAfterSameUserLogsInAgain() {
        String owner = "session-cycle-" + UUID.randomUUID();
        String identifier = "FR13-CYCLE-" + UUID.randomUUID();
        SessionState.login(owner);
        LivestockDAO oldDao = new LivestockDAO();
        int animalId = -1;

        try {
            assertTrue(oldDao.save(new Livestock(
                    "Cow", identifier, "2026-10-25")));
            animalId = oldDao.findAll().stream()
                    .filter(a -> identifier.equals(a.getIdentifier()))
                    .findFirst().orElseThrow().getId();
            final int savedId = animalId;

            SessionState.logout();
            SessionState.login(owner);
            assertTrue(oldDao.findAll().stream()
                    .noneMatch(a -> a.getId() == savedId),
                    "Old DAO must not regain access after re-login");
            assertFalse(oldDao.delete(savedId));

            // A fresh DAO in the new session should still work.
            LivestockDAO freshDao = new LivestockDAO();
            assertTrue(freshDao.findAll().stream()
                    .anyMatch(a -> a.getId() == savedId));
        } finally {
            SessionState.logout();
            if (animalId > 0) {
                new LivestockDAO(owner).delete(animalId);
            }
        }
    }

    @Test
    void unauthenticatedDaoCannotReadOrWrite() {
        SessionState.logout();
        LivestockDAO dao = new LivestockDAO();
        assertTrue(dao.findAll().isEmpty());
        assertFalse(dao.save(new Livestock(
                "Cow", "NO-LOGIN-" + UUID.randomUUID(), "2026-10-25")));
        assertFalse(dao.delete(1));
    }
}
