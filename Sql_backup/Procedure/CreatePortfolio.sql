CREATE DEFINER=`admin_admin`@`%` PROCEDURE `CreatePortfolio`(IN userId VARCHAR(36),IN portfolio_name VARCHAR(100), in filename VARCHAR(200), in description VARCHAR(500), in category varchar(100))
BEGIN

DEClARE existing_portfolio_count int DEFAULT 0;
DEClARE portfolio_id VARCHAR(36) DEFAULT null;
DECLARE category_id VARCHAR(36) default null;

	SELECT id
    Into category_id
    From portfolio_category
    Where portfolio_category.category = category;
	IF category_id != null THEN
		Select COUNT(*)
		Into existing_portfolio_count
		From account_portfolio
		Where account_id = userId
		and name = portfolio_name;

		IF existing_portfolio_count < 1 THEN

			set portfolio_id = UUID();
			 Insert
			Into account_portfolio (id, account_id, name, description, portfolio_image)
			values (portfolio_id, userId, portfolio_name, description, filename);

		ELSE
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'PORTFOLIO_DUPLICATE';
		END IF;

	else
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'INCORRECT_CATEGORY';
	END IF;

END