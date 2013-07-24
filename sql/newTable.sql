CREATE TABLE clickstat (
	[id] [varchar] (70) NOT NULL ,
	[num] [bigint] NOT NULL 
)
GO

CREATE UNIQUE 
  INDEX [idx_cs_id] ON clickstat ([id])
GO

CREATE 
  INDEX [idx_cs_num] ON clickstat ([num]  desc )
GO
