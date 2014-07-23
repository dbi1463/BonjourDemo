//
//  BonjourDiscoveredServiceCell.h
//  BonjouriOS
//
//  Created by Spirit on 7/23/14.
//  Copyright (c) 2014 funymph. All rights reserved.
//

#import <UIKit/UIKit.h>

FOUNDATION_EXPORT NSString* const BonjourDiscoveredServiceCellIdentifier;

@interface BonjourDiscoveredServiceCell : UITableViewCell

@property (nonatomic) NSNetService* service;

@property (weak, nonatomic) IBOutlet UILabel* serviceNameLabel;

@end
