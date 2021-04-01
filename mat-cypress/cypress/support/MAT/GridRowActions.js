export const doubleClickRow = (row) => {
  cy.get(row).click();

  cy.get(row).dblclick();
};

export const selectRow = (row) => {
  cy.get(row).click();

  cy.get(row).click();
};
